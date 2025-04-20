<template>
  <div class="weekly-revenue">
    <h2>Weekly Revenue Statistics</h2>
    
    <!-- error alert -->
    <el-alert
      v-if="hasError"
      :title="errorMessage"
      type="error"
      :closable="false"
      show-icon
    >
      <template #default>
        <div class="error-actions">
          <el-button type="primary" @click="logoutAndRedirect">Re-login</el-button>
        </div>
      </template>
    </el-alert>
    
    <div v-if="!hasError">
      <!-- week selector -->
      <el-card class="week-selector-card">
        <template #header>
          <div class="card-header">
            <span>Select Week</span>
            <el-button type="primary" @click="updateWeeklyRevenue">Update Data</el-button>
          </div>
        </template>
        <div class="week-selector">
          <el-date-picker
            v-model="selectedDate"
            type="week"
            format="第 ww 周"
            value-format="YYYY-MM-DD"
            placeholder="Select Week"
            @change="fetchWeeklyRevenueByDate"
          />
          <div class="week-navigation">
            <el-button type="default" @click="selectPreviousWeek"><i class="el-icon-arrow-left"></i> Previous Week</el-button>
            <el-button type="primary" @click="selectCurrentWeek">Current Week</el-button>
            <el-button type="default" @click="selectNextWeek">Next Week <i class="el-icon-arrow-right"></i></el-button>
          </div>
        </div>
      </el-card>

      <!-- 新增：视图切换选项卡 -->
      <el-tabs v-model="activeView" class="view-tabs">
        <el-tab-pane label="Weekly Overview" name="weekly">
          <!-- 原有的周收入统计内容 -->
          <el-card v-loading="loading" class="current-week-card">
            <template #header>
              <div class="card-header">
                <span>{{ getSelectedWeekLabel() }} Revenue Statistics</span>
              </div>
            </template>
            <div v-if="currentWeekRevenue" class="revenue-details">
              <div class="revenue-item">
                <span class="label">Hourly Revenue:</span>
                <span class="value">¥{{ currentWeekRevenue.hourlyRevenue }}</span>
              </div>
              <div class="revenue-item">
                <span class="label">4-Hour Revenue:</span>
                <span class="value">¥{{ currentWeekRevenue.fourHoursRevenue }}</span>
              </div>
              <div class="revenue-item">
                <span class="label">Daily Revenue:</span>
                <span class="value">¥{{ currentWeekRevenue.dailyRevenue }}</span>
              </div>
              <div class="revenue-item">
                <span class="label">Weekly Revenue:</span>
                <span class="value">¥{{ currentWeekRevenue.weeklyRevenue }}</span>
              </div>
              <div class="revenue-item total">
                <span class="label">Total Revenue:</span>
                <span class="value">¥{{ currentWeekRevenue.totalRevenue }}</span>
              </div>
              <div class="revenue-item">
                <span class="label">Order Count:</span>
                <span class="value">{{ currentWeekRevenue.ordersCount }}</span>
              </div>
              <!-- Top Performing Revenue Type -->
              <div class="revenue-item top-performing" v-if="topPerformingType">
                <span class="label">Top Performing Type:</span>
                <span class="value">{{ topPerformingType.label }} (¥{{ topPerformingType.value }})</span>
              </div>
            </div>
            <div v-else-if="!loading" class="no-data">
              <el-empty description="No revenue data" />
            </div>
          </el-card>

          <!-- revenue pie chart -->
          <el-card v-if="currentWeekRevenue" class="chart-card">
            <template #header>
              <div class="card-header">
                <span>{{ getSelectedWeekLabel() }} Revenue Distribution</span>
              </div>
            </template>
            <div class="chart-container">
              <v-chart :option="pieChartOption" autoresize />
            </div>
          </el-card>

          <!-- revenue trend analysis -->
          <el-card v-if="currentWeekRevenue" class="analysis-card">
            <template #header>
              <div class="card-header">
                <span>Revenue Analysis</span>
              </div>
            </template>
            <div class="analysis-content">
              <div class="analysis-highlight">
                <div class="highlight-icon" :class="getTopPerformingClass()">
                  <i :class="getTopPerformingIcon()"></i>
                </div>
                <div class="highlight-text">
                  <h3>{{ topPerformingType ? topPerformingType.label : 'No data' }} rental generates the most revenue</h3>
                  <p>Making up {{ topPerformingType ? getPercentage(topPerformingType.value, currentWeekRevenue.totalRevenue) : '0' }}% of total weekly revenue</p>
                </div>
              </div>
              
              <div class="analysis-details">
                <el-progress 
                  :percentage="getPercentage(currentWeekRevenue.hourlyRevenue, currentWeekRevenue.totalRevenue)" 
                  :stroke-width="15" 
                  :format="() => `Hourly: ¥${currentWeekRevenue.hourlyRevenue}`" 
                  color="#409EFF">
                </el-progress>
                <el-progress 
                  :percentage="getPercentage(currentWeekRevenue.fourHoursRevenue, currentWeekRevenue.totalRevenue)" 
                  :stroke-width="15" 
                  :format="() => `4-Hour: ¥${currentWeekRevenue.fourHoursRevenue}`" 
                  color="#67C23A">
                </el-progress>
                <el-progress 
                  :percentage="getPercentage(currentWeekRevenue.dailyRevenue, currentWeekRevenue.totalRevenue)" 
                  :stroke-width="15" 
                  :format="() => `Daily: ¥${currentWeekRevenue.dailyRevenue}`" 
                  color="#E6A23C">
                </el-progress>
                <el-progress 
                  :percentage="getPercentage(currentWeekRevenue.weeklyRevenue, currentWeekRevenue.totalRevenue)" 
                  :stroke-width="15" 
                  :format="() => `Weekly: ¥${currentWeekRevenue.weeklyRevenue}`" 
                  color="#F56C6C">
                </el-progress>
              </div>

              <div class="analysis-recommendation">
                <h4>Revenue Optimization Suggestions:</h4>
                <p>{{ getRevenueRecommendation() }}</p>
              </div>
            </div>
          </el-card>
        </el-tab-pane>
        
        <!-- 新增：每日收入视图选项卡 -->
        <el-tab-pane label="Daily Breakdown" name="daily">
          <!-- 每日收入统计卡片 -->
          <el-card v-loading="dailyLoading" class="daily-revenue-card">
            <template #header>
              <div class="card-header">
                <span>{{ getSelectedWeekLabel() }} Daily Revenue Breakdown</span>
                <el-button type="primary" size="small" @click="exportDailyRevenue">
                  Export Report
                </el-button>
              </div>
            </template>
            
            <div v-if="dailyRevenues.length" class="daily-revenue-content">
              <!-- 每日收入柱状图 -->
              <div class="chart-container">
                <v-chart :option="dailyChartOption" autoresize />
              </div>
              
              <!-- 流行租赁日分析 -->
              <div class="popular-days-analysis">
                <h3>Popular Hire Days Analysis</h3>
                <div v-if="popularDay" class="analysis-highlight">
                  <div class="highlight-icon" :class="'day-' + popularDay.dayOfWeek">
                    <i class="el-icon-date"></i>
                  </div>
                  <div class="highlight-text">
                    <h3>{{ popularDay.dayOfWeekName }} is the most popular day</h3>
                    <p>With {{ popularDay.ordersCount }} orders and ¥{{ popularDay.totalRevenue }} revenue</p>
                  </div>
                </div>
                
                <!-- 折扣信息 -->
                <div class="discount-analysis">
                  <h4>Discount Impact</h4>
                  <p>Total discount amount this week: ¥{{ getTotalDiscountAmount() }}</p>
                  <p>Most discounts given on: {{ getMostDiscountedDay() }}</p>
                </div>
              </div>
              
              <!-- 每日收入详情表格 -->
              <el-table :data="dailyRevenues" stripe style="width: 100%" class="daily-table">
                <el-table-column prop="dayOfWeekName" label="Day" width="100" />
                <el-table-column prop="hourlyRevenue" label="1hr Revenue" width="120">
                  <template #default="scope">
                    ¥{{ scope.row.hourlyRevenue }}
                  </template>
                </el-table-column>
                <el-table-column prop="fourHoursRevenue" label="4hr Revenue" width="120">
                  <template #default="scope">
                    ¥{{ scope.row.fourHoursRevenue }}
                  </template>
                </el-table-column>
                <el-table-column prop="dailyRevenue" label="Day Revenue" width="120">
                  <template #default="scope">
                    ¥{{ scope.row.dailyRevenue }}
                  </template>
                </el-table-column>
                <el-table-column prop="weeklyRevenue" label="Week Revenue" width="120">
                  <template #default="scope">
                    ¥{{ scope.row.weeklyRevenue }}
                  </template>
                </el-table-column>
                <el-table-column prop="totalRevenue" label="Total Revenue" width="120">
                  <template #default="scope">
                    <span :class="{ 'highlight-revenue': isPopularDay(scope.row) }">
                      ¥{{ scope.row.totalRevenue }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="ordersCount" label="Orders" width="100" />
                <el-table-column prop="totalDiscount" label="Discount" width="120">
                  <template #default="scope">
                    ¥{{ scope.row.totalDiscount }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
            
            <div v-else-if="!dailyLoading" class="no-data">
              <el-empty description="No daily revenue data" />
            </div>
          </el-card>
        </el-tab-pane>
      </el-tabs>

    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart, LineChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

// 注册 ECharts 必须的组件
use([
  CanvasRenderer,
  PieChart,
  BarChart,
  LineChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent
])

export default {
  name: 'WeeklyRevenue',
  components: {
    VChart
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const currentWeekRevenue = ref(null)
    const selectedDate = ref('')
    const loading = ref(false)
    const hasError = ref(false)
    const errorMessage = ref('')
    const topPerformingType = ref(null)
    const activeView = ref('weekly')
    const dailyLoading = ref(false)
    const dailyRevenues = ref([])
    const popularDay = ref(null)

    // 获取当前日期（格式：YYYY-MM-DD）
    const getCurrentDate = () => {
      const date = new Date()
      return date.toISOString().split('T')[0]
    }

    // 验证是否有管理员权限
    const checkAdminPermission = () => {
      const token = store.getters.token
      const role = store.getters.user?.role
      
      if (!token) {
        errorMessage.value = '请先登录'
        hasError.value = true
        return false
      }
      
      if (role !== 0) {
        errorMessage.value = '权限不足，需要管理员权限'
        hasError.value = true
        return false
      }
      
      return true
    }

    // 重新登录并重定向
    const logoutAndRedirect = () => {
      store.dispatch('logout')
      router.push('/login?redirect=' + encodeURIComponent(router.currentRoute.value.fullPath))
    }

    // 分析最佳表现的租赁类型
    const analyzeTopPerformingType = () => {
      if (!currentWeekRevenue.value) return null
      
      const types = [
        { label: 'Hourly', value: parseFloat(currentWeekRevenue.value.hourlyRevenue), icon: 'el-icon-time', class: 'hourly' },
        { label: '4-Hour', value: parseFloat(currentWeekRevenue.value.fourHoursRevenue), icon: 'el-icon-tickets', class: 'four-hours' },
        { label: 'Daily', value: parseFloat(currentWeekRevenue.value.dailyRevenue), icon: 'el-icon-date', class: 'daily' },
        { label: 'Weekly', value: parseFloat(currentWeekRevenue.value.weeklyRevenue), icon: 'el-icon-calendar', class: 'weekly' }
      ]
      
      topPerformingType.value = types.reduce((max, type) => type.value > max.value ? type : max, { value: 0 })
      
      if (topPerformingType.value.value === 0) {
        topPerformingType.value = { label: 'None', value: 0, icon: 'el-icon-warning', class: 'default' }
      }
      
      return topPerformingType.value
    }

    // 获取顶级表现类型的图标
    const getTopPerformingIcon = () => {
      if (!topPerformingType.value) return 'el-icon-warning'
      return topPerformingType.value.icon
    }

    // 获取顶级表现类型的CSS类
    const getTopPerformingClass = () => {
      if (!topPerformingType.value) return 'default'
      return topPerformingType.value.class
    }

    // 计算百分比
    const getPercentage = (value, total) => {
      if (!value || !total || parseFloat(total) === 0) return 0
      return Math.round((parseFloat(value) / parseFloat(total)) * 100)
    }

    // 获取收入建议
    const getRevenueRecommendation = () => {
      if (!currentWeekRevenue.value || !topPerformingType.value) return '数据不足，无法提供建议。'
      
      const highValue = topPerformingType.value.value
      const totalRevenue = parseFloat(currentWeekRevenue.value.totalRevenue)
      const percentage = getPercentage(highValue, totalRevenue)
      
      if (percentage >= 70) {
        return `您的${topPerformingType.value.label}租赁非常受欢迎，占总收入的${percentage}%。考虑增加此类型滑板车的数量并优化定价策略。`
      } else if (percentage >= 40) {
        return `${topPerformingType.value.label}租赁表现良好，占总收入的${percentage}%。可以考虑针对此类型进行更多促销活动。`
      } else {
        return `您的收入分布相对均衡。考虑分析客户偏好，优化所有租赁类型的体验和价格。`
      }
    }

    // 根据日期获取周收入
    const fetchWeeklyRevenueByDate = async (date) => {
      if (!checkAdminPermission()) return
      
      try {
        loading.value = true
        const response = await axios.get('/api/weekly-revenue/by-date', {
          params: {
            date: date || selectedDate.value
          },
          headers: {
            Authorization: `Bearer ${store.getters.token}`
          }
        })
        currentWeekRevenue.value = response.data
        analyzeTopPerformingType()
        
        // 在获取周收入后，也获取每日收入
        if (activeView.value === 'daily') {
          await fetchDailyRevenuesInWeek(date || selectedDate.value)
        }
      } catch (error) {
        console.error('API错误详情:', error.response || error)
        
        if (error.response?.status === 401) {
          errorMessage.value = '身份验证失败，请重新登录'
          hasError.value = true
        } else if (error.response?.status === 403) {
          errorMessage.value = '权限不足，需要管理员权限'
          hasError.value = true
        } else {
          ElMessage.error('获取周收入失败：' + (error.response?.data || error.message))
          currentWeekRevenue.value = null
          topPerformingType.value = null
        }
      } finally {
        loading.value = false
      }
    }

    // 获取当前周收入
    const fetchCurrentWeekRevenue = async () => {
      if (!checkAdminPermission()) return
      
      try {
        loading.value = true
        const response = await axios.get('/api/weekly-revenue/current', {
          headers: {
            Authorization: `Bearer ${store.getters.token}`
          }
        })
        currentWeekRevenue.value = response.data
        // 更新selectedDate为当前周
        selectedDate.value = getCurrentDate()
        analyzeTopPerformingType()
        
        // 在获取周收入后，也获取每日收入
        if (activeView.value === 'daily') {
          await fetchDailyRevenuesInWeek(selectedDate.value)
        }
      } catch (error) {
        console.error('API错误详情:', error.response || error)
        
        if (error.response?.status === 401) {
          errorMessage.value = '身份验证失败，请重新登录'
          hasError.value = true
        } else if (error.response?.status === 403) {
          errorMessage.value = '权限不足，需要管理员权限'
          hasError.value = true
        } else {
          ElMessage.error('获取当前周收入失败：' + (error.response?.data || error.message))
          currentWeekRevenue.value = null
          topPerformingType.value = null
        }
      } finally {
        loading.value = false
      }
    }

    // 手动更新收入统计
    const updateWeeklyRevenue = async () => {
      if (!checkAdminPermission()) return
      
      try {
        loading.value = true
        await axios.post('/api/weekly-revenue/update', null, {
          headers: {
            Authorization: `Bearer ${store.getters.token}`
          }
        })
        ElMessage.success('收入统计更新成功')
        // 如果当前选择的是本周，则刷新数据
        if (isCurrentWeek()) {
          await fetchCurrentWeekRevenue()
        } else {
          await fetchWeeklyRevenueByDate()
        }
        
        // 如果是每日视图，也刷新每日数据
        if (activeView.value === 'daily') {
          await fetchDailyRevenuesInWeek(selectedDate.value)
        }
      } catch (error) {
        console.error('API错误详情:', error.response || error)
        
        if (error.response?.status === 401) {
          errorMessage.value = '身份验证失败，请重新登录'
          hasError.value = true
        } else if (error.response?.status === 403) {
          errorMessage.value = '权限不足，需要管理员权限'
          hasError.value = true
        } else {
          ElMessage.error('更新收入统计失败：' + (error.response?.data || error.message))
        }
      } finally {
        loading.value = false
      }
    }

    // 判断所选日期是否是当前周
    const isCurrentWeek = () => {
      const today = new Date()
      const startOfWeek = new Date(today)
      // 设置为本周一
      startOfWeek.setDate(today.getDate() - today.getDay() + 1)
      startOfWeek.setHours(0, 0, 0, 0)
      
      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6)
      endOfWeek.setHours(23, 59, 59, 999)

      const selectedDateObj = new Date(selectedDate.value)
      
      return selectedDateObj >= startOfWeek && selectedDateObj <= endOfWeek
    }

    // 获取所选周的标签
    const getSelectedWeekLabel = () => {
      if (!selectedDate.value) return '选中周'
      
      const date = new Date(selectedDate.value)
      const year = date.getFullYear()
      
      // 计算周数
      const firstDayOfYear = new Date(year, 0, 1)
      const pastDaysOfYear = (date - firstDayOfYear) / 86400000
      const weekNum = Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7)
      
      // 获取本周的开始日期和结束日期
      const startOfWeek = new Date(date)
      startOfWeek.setDate(date.getDate() - date.getDay() + 1) // 设置为本周一
      
      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6) // 设置为本周日

      const formatDate = (d) => {
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
      }
      
      return `${year}年第${weekNum}周 (${formatDate(startOfWeek)} 至 ${formatDate(endOfWeek)})`
    }

    // 选择上一周
    const selectPreviousWeek = () => {
      const date = new Date(selectedDate.value)
      date.setDate(date.getDate() - 7)
      selectedDate.value = date.toISOString().split('T')[0]
      fetchWeeklyRevenueByDate(selectedDate.value)
    }

    // 选择当前周
    const selectCurrentWeek = () => {
      selectedDate.value = getCurrentDate()
      fetchCurrentWeekRevenue()
    }

    // 选择下一周
    const selectNextWeek = () => {
      const date = new Date(selectedDate.value)
      date.setDate(date.getDate() + 7)
      selectedDate.value = date.toISOString().split('T')[0]
      fetchWeeklyRevenueByDate(selectedDate.value)
    }

    // 饼图配置
    const pieChartOption = computed(() => {
      if (!currentWeekRevenue.value) return {}
      
      return {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['按小时收入', '按4小时收入', '按天收入', '按周收入']
        },
        series: [
          {
            name: '收入分布',
            type: 'pie',
            radius: '60%',
            center: ['50%', '50%'],
            data: [
              {value: parseFloat(currentWeekRevenue.value.hourlyRevenue), name: '按小时收入'},
              {value: parseFloat(currentWeekRevenue.value.fourHoursRevenue), name: '按4小时收入'},
              {value: parseFloat(currentWeekRevenue.value.dailyRevenue), name: '按天收入'},
              {value: parseFloat(currentWeekRevenue.value.weeklyRevenue), name: '按周收入'}
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            label: {
              formatter: '{b}: ¥{c} ({d}%)'
            }
          }
        ]
      }
    })
    
    // 新增：获取每日收入数据
    const fetchDailyRevenuesInWeek = async (date) => {
      if (!checkAdminPermission()) return
      
      try {
        dailyLoading.value = true
        const response = await axios.get('/api/weekly-revenue/daily', {
          params: {
            weekStartDate: date
          },
          headers: {
            Authorization: `Bearer ${store.getters.token}`
          }
        })
        
        dailyRevenues.value = response.data
        analyzePopularDay()
      } catch (error) {
        console.error('API错误详情:', error.response || error)
        
        if (error.response?.status === 401) {
          errorMessage.value = '身份验证失败，请重新登录'
          hasError.value = true
        } else if (error.response?.status === 403) {
          errorMessage.value = '权限不足，需要管理员权限'
          hasError.value = true
        } else {
          ElMessage.error('获取每日收入统计失败：' + (error.response?.data || error.message))
          dailyRevenues.value = []
        }
      } finally {
        dailyLoading.value = false
      }
    }
    
    // 新增：分析最受欢迎的日期
    const analyzePopularDay = () => {
      if (!dailyRevenues.value || dailyRevenues.value.length === 0) {
        popularDay.value = null
        return
      }
      
      // 找出收入最高的日期
      popularDay.value = dailyRevenues.value.reduce((max, day) => 
        parseFloat(day.totalRevenue) > parseFloat(max.totalRevenue) ? day : max, 
        dailyRevenues.value[0]
      )
    }
    
    // 新增：日收入柱状图配置
    const dailyChartOption = computed(() => {
      if (!dailyRevenues.value || dailyRevenues.value.length === 0) return {}
      
      const days = dailyRevenues.value.map(day => day.dayOfWeekName)
      const hourlyData = dailyRevenues.value.map(day => parseFloat(day.hourlyRevenue))
      const fourHoursData = dailyRevenues.value.map(day => parseFloat(day.fourHoursRevenue))
      const dailyData = dailyRevenues.value.map(day => parseFloat(day.dailyRevenue))
      const weeklyData = dailyRevenues.value.map(day => parseFloat(day.weeklyRevenue))
      const ordersData = dailyRevenues.value.map(day => day.ordersCount)
      
      return {
        title: {
          text: '每日收入细分',
          subtext: '按租赁类型显示'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function(params) {
            let tooltip = params[0].name + '<br/>';
            let total = 0;
            
            params.forEach(param => {
              tooltip += param.seriesName + ': ¥' + param.value.toFixed(2) + '<br/>';
              if (param.seriesIndex < 4) { // 不包括订单数量
                total += param.value;
              }
            });
            
            tooltip += '<br/>总收入: ¥' + total.toFixed(2);
            return tooltip;
          }
        },
        legend: {
          data: ['按小时收入', '按4小时收入', '按天收入', '按周收入', '订单数量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: days
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '收入 (¥)',
            position: 'left',
            axisLabel: {
              formatter: '¥{value}'
            }
          },
          {
            type: 'value',
            name: '订单数量',
            position: 'right',
            axisLine: {
              show: true,
              lineStyle: {
                color: '#5470C6'
              }
            },
            axisLabel: {
              formatter: '{value}'
            }
          }
        ],
        series: [
          {
            name: '按小时收入',
            type: 'bar',
            stack: '收入',
            emphasis: {
              focus: 'series'
            },
            data: hourlyData,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '按4小时收入',
            type: 'bar',
            stack: '收入',
            emphasis: {
              focus: 'series'
            },
            data: fourHoursData,
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '按天收入',
            type: 'bar',
            stack: '收入',
            emphasis: {
              focus: 'series'
            },
            data: dailyData,
            itemStyle: {
              color: '#E6A23C'
            }
          },
          {
            name: '按周收入',
            type: 'bar',
            stack: '收入',
            emphasis: {
              focus: 'series'
            },
            data: weeklyData,
            itemStyle: {
              color: '#F56C6C'
            }
          },
          {
            name: '订单数量',
            type: 'line',
            yAxisIndex: 1,
            data: ordersData,
            itemStyle: {
              color: '#5470C6'
            },
            symbolSize: 8,
            lineStyle: {
              width: 2
            }
          }
        ]
      }
    });
    
    // 新增：判断是否为热门日
    const isPopularDay = (day) => {
      if (!popularDay.value) return false;
      return day.dayOfWeek === popularDay.value.dayOfWeek;
    }
    
    // 新增：获取总折扣金额
    const getTotalDiscountAmount = () => {
      if (!dailyRevenues.value || dailyRevenues.value.length === 0) return '0.00';
      
      const total = dailyRevenues.value.reduce((sum, day) => 
        sum + parseFloat(day.totalDiscount || 0), 0
      );
      
      return total.toFixed(2);
    }
    
    // 新增：获取最多折扣的日期
    const getMostDiscountedDay = () => {
      if (!dailyRevenues.value || dailyRevenues.value.length === 0) return 'None';
      
      const mostDiscountDay = dailyRevenues.value.reduce((max, day) => 
        parseFloat(day.totalDiscount || 0) > parseFloat(max.totalDiscount || 0) ? day : max, 
        dailyRevenues.value[0]
      );
      
      return mostDiscountDay.dayOfWeekName;
    }
    
    // 新增：导出收入报表
    const exportDailyRevenue = () => {
      if (!dailyRevenues.value || dailyRevenues.value.length === 0) {
        ElMessage.warning('没有可导出的数据');
        return;
      }
      
      // 构建CSV内容
      let csvContent = "日期,星期几,小时收入,4小时收入,日收入,周收入,总收入,订单数量,折扣总额\n";
      
      dailyRevenues.value.forEach(day => {
        const date = new Date(day.date);
        const dateStr = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
        
        csvContent += `${dateStr},${day.dayOfWeekName},${day.hourlyRevenue},${day.fourHoursRevenue},${day.dailyRevenue},${day.weeklyRevenue},${day.totalRevenue},${day.ordersCount},${day.totalDiscount}\n`;
      });
      
      // 创建下载链接
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.setAttribute('href', url);
      link.setAttribute('download', `daily-revenue-${selectedDate.value}.csv`);
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      
      ElMessage.success('报表导出成功');
    }

    // Watch for changes in currentWeekRevenue and update top performing type
    watch(currentWeekRevenue, (newValue) => {
      if (newValue) {
        analyzeTopPerformingType()
      }
    })
    
    // 监听选项卡变化，当切换到每日视图时加载数据
    watch(activeView, (newTab) => {
      if (newTab === 'daily' && (!dailyRevenues.value || dailyRevenues.value.length === 0)) {
        fetchDailyRevenuesInWeek(selectedDate.value)
      }
    })

    onMounted(() => {
      // 确保用户已登录且有权限
      if (checkAdminPermission()) {
        fetchCurrentWeekRevenue()
      }
    })

    return {
      currentWeekRevenue,
      selectedDate,
      loading,
      hasError,
      errorMessage,
      topPerformingType,
      pieChartOption,
      activeView,
      dailyRevenues,
      dailyLoading,
      popularDay,
      dailyChartOption,
      logoutAndRedirect,
      fetchCurrentWeekRevenue,
      fetchWeeklyRevenueByDate,
      updateWeeklyRevenue,
      getSelectedWeekLabel,
      selectPreviousWeek,
      selectCurrentWeek,
      selectNextWeek,
      getTopPerformingIcon,
      getTopPerformingClass,
      getPercentage,
      getRevenueRecommendation,
      fetchDailyRevenuesInWeek,
      isPopularDay,
      getTotalDiscountAmount,
      getMostDiscountedDay,
      exportDailyRevenue
    }
  }
}
</script>

<style scoped>
.weekly-revenue {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
  color: #303133;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.week-selector {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.week-navigation {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.revenue-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  padding: 10px;
}

.revenue-item {
  display: flex;
  flex-direction: column;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.revenue-item.total {
  background-color: #ecf5ff;
  font-weight: bold;
}

.revenue-item.top-performing {
  background-color: #f0f9eb;
  border-left: 4px solid #67C23A;
  font-weight: bold;
}

.label {
  color: #606266;
  margin-bottom: 5px;
}

.value {
  font-size: 1.2em;
  color: #303133;
}

.chart-container {
  height: 400px;
  width: 100%;
}

.no-data {
  padding: 40px;
  display: flex;
  justify-content: center;
}

.week-selector-card,
.current-week-card,
.chart-card,
.analysis-card {
  margin-bottom: 20px;
}

.error-actions {
  margin-top: 10px;
}

.analysis-content {
  padding: 10px;
}

.analysis-highlight {
  display: flex;
  align-items: center;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.highlight-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  color: white;
  font-size: 24px;
}

.highlight-icon.hourly {
  background-color: #409EFF;
}

.highlight-icon.four-hours {
  background-color: #67C23A;
}

.highlight-icon.daily {
  background-color: #E6A23C;
}

.highlight-icon.weekly {
  background-color: #F56C6C;
}

.highlight-icon.default {
  background-color: #909399;
}

.highlight-text h3 {
  margin: 0;
  color: #303133;
}

.highlight-text p {
  margin: 5px 0 0;
  color: #606266;
}

.analysis-details {
  margin-bottom: 20px;
}

.analysis-details .el-progress {
  margin-bottom: 15px;
}

.analysis-recommendation {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  border-left: 4px solid #409EFF;
}

.analysis-recommendation h4 {
  margin-top: 0;
  color: #303133;
}

.analysis-recommendation p {
  margin-bottom: 0;
  color: #606266;
}
</style> 