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

      <!-- current week revenue card -->
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
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
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

    // 获取当前日期（格式：YYYY-MM-DD）
    const getCurrentDate = () => {
      const date = new Date()
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    }

    // 初始化选择当前周
    selectedDate.value = getCurrentDate()

    // 登出并重定向到登录页面
    const logoutAndRedirect = () => {
      store.dispatch('logout')
      router.push('/login')
    }

    // 计算top performing revenue type
    const analyzeTopPerformingType = () => {
      if (!currentWeekRevenue.value) {
        topPerformingType.value = null
        return
      }
      
      const types = [
        { type: 'hourly', label: 'Hourly', value: parseFloat(currentWeekRevenue.value.hourlyRevenue) },
        { type: 'fourHours', label: '4-Hour', value: parseFloat(currentWeekRevenue.value.fourHoursRevenue) },
        { type: 'daily', label: 'Daily', value: parseFloat(currentWeekRevenue.value.dailyRevenue) },
        { type: 'weekly', label: 'Weekly', value: parseFloat(currentWeekRevenue.value.weeklyRevenue) }
      ]
      
      // Find the type with the highest revenue
      topPerformingType.value = types.reduce((max, current) => 
        (current.value > max.value) ? current : max, types[0])
    }

    // 获取top performing type的图标
    const getTopPerformingIcon = () => {
      if (!topPerformingType.value) return 'el-icon-data-analysis'
      
      switch (topPerformingType.value.type) {
        case 'hourly': return 'el-icon-time'
        case 'fourHours': return 'el-icon-alarm-clock'
        case 'daily': return 'el-icon-date'
        case 'weekly': return 'el-icon-calendar'
        default: return 'el-icon-data-analysis'
      }
    }

    // 获取top performing type的CSS类
    const getTopPerformingClass = () => {
      if (!topPerformingType.value) return 'default'
      
      switch (topPerformingType.value.type) {
        case 'hourly': return 'hourly'
        case 'fourHours': return 'four-hours'
        case 'daily': return 'daily'
        case 'weekly': return 'weekly'
        default: return 'default'
      }
    }

    // 计算百分比
    const getPercentage = (value, total) => {
      if (!value || !total || parseFloat(total) === 0) return 0
      return Math.round((parseFloat(value) / parseFloat(total)) * 100)
    }

    // 获取收入优化建议
    const getRevenueRecommendation = () => {
      if (!currentWeekRevenue.value || !topPerformingType.value) return 'No data available for recommendations.'
      
      switch (topPerformingType.value.type) {
        case 'hourly':
          return 'Hourly rentals are performing well. Consider increasing hourly rates during peak hours or offering special hourly packages for frequent users.'
        case 'fourHours':
          return '4-Hour rentals are your top performers. Market this option more prominently and consider enhancing the value proposition of these mid-duration rentals.'
        case 'daily':
          return 'Daily rentals are generating the most revenue. Consider creating day passes or bundled day offers with additional benefits to increase sales.'
        case 'weekly':
          return 'Weekly rentals are your highest revenue generators. Focus marketing on long-term rental benefits and consider loyalty discounts for repeat weekly renters.'
        default:
          return 'Review your pricing strategy across all rental durations to optimize revenue.'
      }
    }

    // 检查用户权限
    const checkAdminPermission = () => {
      const user = store.getters.user
      if (!user) {
        errorMessage.value = '未登录，请先登录系统'
        hasError.value = true
        return false
      }
      
      if (user.role !== 0) {
        errorMessage.value = '您没有访问此页面的权限，需要管理员权限'
        hasError.value = true
        return false
      }
      
      return true
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

    // Watch for changes in currentWeekRevenue and update top performing type
    watch(currentWeekRevenue, (newValue) => {
      if (newValue) {
        analyzeTopPerformingType()
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
      getRevenueRecommendation
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