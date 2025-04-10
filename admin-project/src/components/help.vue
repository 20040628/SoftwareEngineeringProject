<template>
  <div class="weekly-revenue-container">
    <!-- 错误信息显示 -->
    <el-alert
        v-if="hasError"
        :title="errorMessage"
        type="error"
        show-icon
        :closable="false"
        class="error-alert"
    >
      <el-button v-if="errorMessage.includes('未登录')" type="primary" @click="logoutAndRedirect">
        立即登录
      </el-button>
    </el-alert>

    <!-- 无错误时显示主要内容 -->
    <div v-else>
      <!-- 标题和控制按钮 -->
      <div class="header-section">
        <h2>周收入分析</h2>
        <div class="controls">
          <el-button-group>
            <el-button
                :disabled="loading"
                @click="selectPreviousWeek"
                icon="el-icon-arrow-left"
            >
              上一周
            </el-button>
            <el-button
                :disabled="loading"
                @click="selectCurrentWeek"
                type="primary"
            >
              本周
            </el-button>
            <el-button
                :disabled="loading"
                @click="selectNextWeek"
                icon="el-icon-arrow-right"
            >
              下一周
            </el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 周选择 -->
      <div class="week-selection">
        <el-card shadow="hover">
          <div slot="header">
            <span>选择周</span>
          </div>
          <div class="week-info">
            <el-date-picker
                v-model="selectedDate"
                type="week"
                format="yyyy 第 WW 周"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions"
                placeholder="选择周"
                @change="handleWeekChange"
                :disabled="loading"
            />
            <div class="week-label">{{ getSelectedWeekLabel() }}</div>
          </div>
        </el-card>
      </div>

      <!-- 图表和最佳表现类型 -->
      <div class="chart-section">
        <el-card shadow="hover">
          <div class="chart-content">
            <div class="chart-container">
              <v-chart
                  v-if="currentWeekRevenue"
                  :option="pieChartOption"
                  :loading="loading"
                  autoresize
              />
              <el-empty v-else description="暂无数据" />
            </div>

            <!-- 最佳表现类型 -->
            <div class="top-performing" v-if="topPerformingType">
              <div class="top-content">
                <div class="top-text">
                  <h3><i :class="getTopPerformingIcon()"></i> 最佳表现类型</h3>
                  <div class="revenue-value">
                    {{ topPerformingType.label }}收入:
                    <span class="amount">¥{{ topPerformingType.value.toFixed(2) }}</span>
                  </div>
                  <div class="recommendation-box">
                    <p class="recommendation">{{ getRevenueRecommendation() }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 收入详情表格 -->
      <div class="details-section">
        <el-card shadow="hover">
          <div slot="header">
            <span>收入详情</span>
          </div>
          <el-table
              :data="tableData"
              border
              style="width: 100%"
              v-loading="loading"
              empty-text="暂无数据"
          >
            <el-table-column prop="weekStartDate" label="周开始日期" width="150" />
            <el-table-column prop="weekEndDate" label="周结束日期" width="150" />
            <el-table-column label="按小时收入" width="180">
              <template #default="{row}">
                ¥{{ parseFloat(row.hourlyRevenue).toFixed(2) }}
                <el-tag size="mini" type="success">
                  {{ getPercentage(row.hourlyRevenue, row.totalRevenue) }}%
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="按4小时收入" width="180">
              <template #default="{row}">
                ¥{{ parseFloat(row.fourHoursRevenue).toFixed(2) }}
                <el-tag size="mini" type="warning">
                  {{ getPercentage(row.fourHoursRevenue, row.totalRevenue) }}%
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="按天收入" width="180">
              <template #default="{row}">
                ¥{{ parseFloat(row.dailyRevenue).toFixed(2) }}
                <el-tag size="mini" type="primary">
                  {{ getPercentage(row.dailyRevenue, row.totalRevenue) }}%
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="按周收入" width="180">
              <template #default="{row}">
                ¥{{ parseFloat(row.weeklyRevenue).toFixed(2) }}
                <el-tag size="mini" type="danger">
                  {{ getPercentage(row.weeklyRevenue, row.totalRevenue) }}%
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="总收入" width="150">
              <template #default="{row}">
                <strong>¥{{ parseFloat(row.totalRevenue).toFixed(2) }}</strong>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>

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

// 注册ECharts组件
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
    const tableData = ref([])

    // 日期选择器选项
    const pickerOptions = {
      disabledDate(time) {
        return time.getTime() > Date.now()
      },
      firstDayOfWeek: 1 // 设置周一为一周的第一天
    }

    // 获取当前日期
    const getCurrentDate = () => {
      const date = new Date()
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    }

    // 初始化当前周
    selectedDate.value = getCurrentDate()

    // 登出
    const logoutAndRedirect = () => {
      store.dispatch('logout')
      router.push('/login')
    }

    // 分析最佳表现类型
    const analyzeTopPerformingType = () => {
      if (!currentWeekRevenue.value) {
        topPerformingType.value = null
        return
      }

      const types = [
        { type: 'hourly', label: '按小时', value: parseFloat(currentWeekRevenue.value.hourlyRevenue) },
        { type: 'fourHours', label: '按4小时', value: parseFloat(currentWeekRevenue.value.fourHoursRevenue) },
        { type: 'daily', label: '按天', value: parseFloat(currentWeekRevenue.value.dailyRevenue) },
        { type: 'weekly', label: '按周', value: parseFloat(currentWeekRevenue.value.weeklyRevenue) }
      ]

      topPerformingType.value = types.reduce((max, current) =>
          (current.value > max.value) ? current : max, types[0])
    }

    // 获取最佳表现类型图标
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

    // 计算百分比
    const getPercentage = (value, total) => {
      if (!value || !total || parseFloat(total) === 0) return 0
      return Math.round((parseFloat(value) / parseFloat(total)) * 100)
    }

    // 获取收入优化建议
    const getRevenueRecommendation = () => {
      if (!currentWeekRevenue.value || !topPerformingType.value) return '暂无数据提供建议'

      switch (topPerformingType.value.type) {
        case 'hourly':
          return '按小时租赁表现良好。考虑在高峰时段提高小时费率或为常客提供特别小时套餐。'
        case 'fourHours':
          return '4小时租赁是您的顶级表现者。更突出地推广此选项，并考虑增强这些中等时长租赁的价值主张。'
        case 'daily':
          return '按天租赁产生的收入最多。考虑创建日票或捆绑日优惠，并提供额外福利以增加销售。'
        case 'weekly':
          return '按周租赁是您的最高收入来源。将营销重点放在长期租赁的好处上，并为重复按周租赁者考虑忠诚度折扣。'
        default:
          return '审查所有租赁时长的定价策略以优化收入。'
      }
    }

    // 检查管理员权限
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

    // 处理周选择变化
    const handleWeekChange = (date) => {
      if (!date) return
      fetchWeeklyRevenueByDate(date)
    }

    // 按日期获取周收入
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
        tableData.value = [response.data] // 正确设置表格数据
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
          tableData.value = []
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
        tableData.value = [response.data] // 正确设置表格数据
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
          tableData.value = []
        }
      } finally {
        loading.value = false
      }
    }

    // 判断是否是当前周
    const isCurrentWeek = () => {
      const today = new Date()
      const startOfWeek = new Date(today)
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

      // 获取本周的开始和结束日期
      const startOfWeek = new Date(date)
      startOfWeek.setDate(date.getDate() - date.getDay() + 1)

      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6)

      const formatDate = (d) => {
        return `${d.getFullYear()}年${String(d.getMonth() + 1).padStart(2, '0')}月${String(d.getDate()).padStart(2, '0')}日`
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
          formatter: '{b}: ¥{c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: ['按小时收入', '按4小时收入', '按天收入', '按周收入']
        },
        series: [
          {
            name: '收入分布',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['40%', '50%'],
            avoidLabelOverlap: true,
            itemStyle: {
              borderRadius: 5,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {d}%',
              fontSize: 12
            },
            labelLine: {
              show: true,
              length: 10,
              length2: 15
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: 'bold'
              }
            },
            data: [
              {value: parseFloat(currentWeekRevenue.value.hourlyRevenue), name: '按小时收入', itemStyle: {color: '#67C23A'}},
              {value: parseFloat(currentWeekRevenue.value.fourHoursRevenue), name: '按4小时收入', itemStyle: {color: '#E6A23C'}},
              {value: parseFloat(currentWeekRevenue.value.dailyRevenue), name: '按天收入', itemStyle: {color: '#409EFF'}},
              {value: parseFloat(currentWeekRevenue.value.weeklyRevenue), name: '按周收入', itemStyle: {color: '#F56C6C'}}
            ]
          }
        ]
      }
    })

    // 监听当前周收入变化
    watch(currentWeekRevenue, (newValue) => {
      if (newValue) {
        analyzeTopPerformingType()
      }
    })

    onMounted(() => {
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
      tableData,
      pieChartOption,
      pickerOptions,
      logoutAndRedirect,
      fetchCurrentWeekRevenue,
      fetchWeeklyRevenueByDate,
      handleWeekChange,
      getSelectedWeekLabel,
      selectPreviousWeek,
      selectCurrentWeek,
      selectNextWeek,
      getTopPerformingIcon,
      getPercentage,
      getRevenueRecommendation
    }
  }
}
</script>

<style scoped>
.weekly-revenue-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.error-alert {
  margin-bottom: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.controls {
  display: flex;
  gap: 10px;
}

.week-selection {
  margin-bottom: 20px;
}

.week-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.week-label {
  font-weight: bold;
  color: #409EFF;
}

.chart-section {
  margin-bottom: 20px;
}

.chart-content {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.chart-container {
  flex: 1;
  min-width: 400px;
  height: 350px; /* 减小图表高度 */
}

.top-performing {
  flex: 1;
  min-width: 300px;
  display: flex;
  align-items: center;
}

.top-content {
  padding: 20px;
  width: 100%;
}

.top-text h3 {
  margin: 0 0 15px 0;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  color: #333;
}

.top-text h3 i {
  font-size: 24px;
  color: #409EFF;
}

.revenue-value {
  margin: 15px 0;
  font-size: 16px;
  color: #666;
}

.amount {
  font-weight: bold;
  color: #F56C6C;
  font-size: 18px;
}

.recommendation-box {
  background-color: #f8f8f8;
  padding: 15px;
  border-radius: 4px;
  border-left: 4px solid #409EFF;
}

.recommendation {
  margin: 0;
  line-height: 1.6;
  color: #555;
  font-size: 14px;
}

.details-section {
  margin-top: 20px;
}

.el-table {
  margin-top: 10px;
}

.el-tag {
  margin-left: 8px;
}

@media (max-width: 768px) {
  .header-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .chart-content {
    flex-direction: column;
  }

  .chart-container {
    min-width: 100%;
    height: 300px;
  }

  .week-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .top-performing {
    margin-top: 20px;
  }
}
</style>