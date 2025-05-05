<template>
  <div class="weekly-revenue">
    <h2 class="title">Weekly Revenue Statistics</h2>

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
            <span>Current Select : {{ getSelectedWeekLabel() }} </span>
          </div>
        </template>

        <div class="week-selector-container">
          <el-date-picker
              v-model="selectedDate"
              type="week"
              format="[Week] ww"
              value-format="YYYY-MM-DD"
              placeholder="Select Week"
              @change="fetchWeeklyRevenueByDate"
              class="custom-date-picker"
          />
          <div class="week-navigation">
            <el-button
                type="default"
                @click="selectPreviousWeek"
                class="nav-button prev-button"
            >
              <i class="el-icon-arrow-left"></i> Previous Week
            </el-button>
            <el-button
                type="primary"
                @click="selectCurrentWeek"
                class="nav-button current-button"
            >
              Current Week
            </el-button>
            <el-button
                type="default"
                @click="selectNextWeek"
                class="nav-button next-button"
            >
              Next Week <i class="el-icon-arrow-right"></i>
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- combined chart and analysis section -->
      <div class="chart-analysis-container">
        <!-- revenue pie chart -->
        <el-card v-if="currentWeekRevenue" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>Revenue Distribution</span>
            </div>
          </template>
          <div class="chart-container">
            <v-chart :option="pieChartOption" autoresize />
          </div>
        </el-card>

        <!-- daily trend analysis -->
        <el-card v-if="currentWeekRevenue" class="analysis-card">
          <template #header>
            <div class="card-header">
              <span>Daily Revenue Trend</span>
            </div>
          </template>
          <div class="analysis-content">
            <div style="margin-bottom: 16px; width: 150px">
              <el-select v-model="selectedRevenueType" placeholder="Select Revenue Type" @change="updateDailyChart">
                <el-option
                    v-for="item in revenueTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </div>
            <div class="line-chart-container">
              <v-chart :option="dailyChartOption" autoresize style="height: 400px;" />
            </div>
          </div>
        </el-card>
      </div>

      <!-- current week revenue card -->
      <el-card v-loading="loading" class="current-week-card">
        <template #header>
          <div class="card-header">
            <span>{{ getSelectedWeekLabel() }} Revenue Analysis</span>
          </div>
        </template>
        <div class="revenue-content-container">
          <div v-if="currentWeekRevenue" class="revenue-details-container">
            <div class="revenue-details">
              <div class="revenue-row">
                <div class="revenue-item">
                  <span class="label">Hourly Revenue:</span>
                  <span class="value">£{{ currentWeekRevenue.hourlyRevenue }}</span>
                </div>
                <div class="revenue-item">
                  <span class="label">4-Hour Revenue:</span>
                  <span class="value">£{{ currentWeekRevenue.fourHoursRevenue }}</span>
                </div>
              </div>
              <div class="revenue-row">
                <div class="revenue-item">
                  <span class="label">Daily Revenue:</span>
                  <span class="value">£{{ currentWeekRevenue.dailyRevenue }}</span>
                </div>
                <div class="revenue-item">
                  <span class="label">Weekly Revenue:</span>
                  <span class="value">£{{ currentWeekRevenue.weeklyRevenue }}</span>
                </div>
              </div>
              <div class="revenue-item total">
                <span class="label">Total Revenue:</span>
                <span class="value">£{{ currentWeekRevenue.totalRevenue }}</span>
              </div>
              <div class="revenue-item order-count">
                <span class="label">Order Count:</span>
                <span class="value">{{ currentWeekRevenue.ordersCount }}</span>
              </div>
              <!-- Top Performing Revenue Type -->
              <div class="revenue-item top-performing" v-if="topPerformingType">
                <span class="label">Top Performing Type:</span>
                <span class="value">{{ topPerformingType.label }} (£{{ topPerformingType.value }})</span>
              </div>
            </div>
          </div>
          <div v-else-if="!loading" class="no-data">
            <el-empty description="No revenue data" />
          </div>
          <div class="analysis-recommendation">
            <h3>Recommendation</h3>
            <!-- Add peak days analysis -->
            <div class="peak-days-analysis" v-if="processedDailyData">
              <h4>Peak Revenue Days:</h4>
              <div class="peak-day-item">
                <span class="peak-label">Highest Hourly Revenue:</span>
                <span class="peak-value">{{ peakDays.hourly.day }} (£{{ peakDays.hourly.value }})</span>
              </div>
              <div class="peak-day-item">
                <span class="peak-label">Highest 4-Hour Revenue:</span>
                <span class="peak-value">{{ peakDays.fourHours.day }} (£{{ peakDays.fourHours.value }})</span>
              </div>
              <div class="peak-day-item">
                <span class="peak-label">Highest Daily Revenue:</span>
                <span class="peak-value">{{ peakDays.daily.day }} (£{{ peakDays.daily.value }})</span>
              </div>
            </div>
            <div class="analysis-highlight">
              <div class="highlight-icon" :class="getTopPerformingClass()">
                <i :class="getTopPerformingIcon()"></i>
              </div>
              <div class="highlight-text">
                <h4>{{ topPerformingType ? topPerformingType.label : 'No data' }} rental generates the most revenue</h4>
                <p>Making up {{ topPerformingType ? getPercentage(topPerformingType.value, currentWeekRevenue.totalRevenue) : '0' }}% of total weekly revenue</p>
              </div>
            </div>
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

// Register required ECharts components
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

    // Daily revenue chart configuration
    // Daily revenue chart configuration
    const dailyChartOption = ref({
      title: {
        text: 'Daily Revenue Trend',
        left: 'center',
      },
      tooltip: {
        trigger: 'axis',
        formatter: (params) => {
          const date = params[0].axisValue
          const value = params[0].data
          return `${date}<br/>${selectedRevenueType.value}: £${value}`
        }
      },
      xAxis: {
        type: 'category',
        data: [],
        name: 'Day',
      },
      yAxis: {
        type: 'value',
        name: 'Revenue (£)',
      },
      series: [
        {
          name: 'Revenue',
          type: 'line',
          smooth: true,
          data: [],
          lineStyle: {
            width: 3,
            color: '#5470C6',
          },
          itemStyle: {
            color: '#5470C6',
          },
        },
      ],
    })

    // Revenue type selector
    const selectedRevenueType = ref('hourlyRevenue')
    const revenueTypeOptions = ref([
      { value: 'hourlyRevenue', label: 'Hourly Revenue' },
      { value: 'fourHoursRevenue', label: '4-Hour Revenue' },
      { value: 'dailyRevenue', label: 'Daily Revenue' }
    ])

    // Process daily revenue data
    const processDailyRevenueData = (revenueData) => {
      const dayNames = {
        1: 'Mon',
        2: 'Tue',
        3: 'Wed',
        4: 'Thu',
        5: 'Fri',
        6: 'Sat',
        7: 'Sun',
      }

      return {
        dates: revenueData.map(item => dayNames[item.dayOfWeek] || item.dayOfWeekName),
        hourlyValues: revenueData.map(item => item.hourlyRevenue),
        fourHoursValues: revenueData.map(item => item.fourHoursRevenue),
        dailyValues: revenueData.map(item => item.dailyRevenue)
      }
    }

    // Update chart based on selected revenue type
    const updateDailyChart = () => {
      if (!processedDailyData.value) return

      switch (selectedRevenueType.value) {
        case 'hourlyRevenue':
          dailyChartOption.value.series[0].data = processedDailyData.value.hourlyValues
          dailyChartOption.value.series[0].name = 'Hourly Revenue'
          break
        case 'fourHoursRevenue':
          dailyChartOption.value.series[0].data = processedDailyData.value.fourHoursValues
          dailyChartOption.value.series[0].name = '4-Hour Revenue'
          break
        case 'dailyRevenue':
          dailyChartOption.value.series[0].data = processedDailyData.value.dailyValues
          dailyChartOption.value.series[0].name = 'Daily Revenue'
          break
      }
    }

    // Store processed daily data
    const processedDailyData = ref(null)

    // Fetch daily revenue data for the selected week
    const fetchDailyRevenue = async () => {
      try {
        const response = await axios.get('/api/weekly-revenue/daily', {
          params: { weekStartDate: selectedDate.value },
          headers: {
            Authorization: `Bearer ${store.getters.token}`
          }
        })

        // Process the response data
        processedDailyData.value = processDailyRevenueData(response.data)
        dailyChartOption.value.xAxis.data = processedDailyData.value.dates

        // Update chart with default selected revenue type
        updateDailyChart()

      } catch (error) {
        console.error('Failed to get daily revenue:', error)
      }
    }

    // Get current date (format: YYYY-MM-DD)
    const getCurrentDate = () => {
      const date = new Date()
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    }

    // Initialize with current week
    selectedDate.value = getCurrentDate()

    // Logout and redirect to login page
    const logoutAndRedirect = () => {
      store.dispatch('logout')
      router.push('/login')
    }

    const peakDays = computed(() => {
      if (!processedDailyData.value) return {
        hourly: { day: 'N/A', value: 0 },
        fourHours: { day: 'N/A', value: 0 },
        daily: { day: 'N/A', value: 0 }
      }

      const findPeakDay = (values) => {
        const maxValue = Math.max(...values)
        const maxIndex = values.indexOf(maxValue)
        return {
          day: processedDailyData.value.dates[maxIndex],
          value: maxValue
        }
      }

      return {
        hourly: findPeakDay(processedDailyData.value.hourlyValues),
        fourHours: findPeakDay(processedDailyData.value.fourHoursValues),
        daily: findPeakDay(processedDailyData.value.dailyValues)
      }
    })

    // Calculate top performing revenue type
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

    // Get top performing type icon
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

    // Get top performing type CSS class
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

    // Calculate percentage
    const getPercentage = (value, total) => {
      if (!value || !total || parseFloat(total) === 0) return 0
      return Math.round((parseFloat(value) / parseFloat(total)) * 100)
    }

    // Get revenue optimization recommendation
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

    // Check admin permission
    const checkAdminPermission = () => {
      const user = store.getters.user
      if (!user) {
        errorMessage.value = 'Not logged in, please login first'
        hasError.value = true
        return false
      }

      if (user.role !== 0) {
        errorMessage.value = 'You do not have permission to access this page, admin role required'
        hasError.value = true
        return false
      }

      return true
    }

    // Get weekly revenue by date
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
        await fetchDailyRevenue()
      } catch (error) {
        console.error('API error details:', error.response || error)

        if (error.response?.status === 401) {
          errorMessage.value = 'Authentication failed, please login again'
          hasError.value = true
        } else if (error.response?.status === 403) {
          errorMessage.value = 'Insufficient permissions, admin role required'
          hasError.value = true
        } else {
          ElMessage.error('Failed to get weekly revenue: ' + (error.response?.data || error.message))
          currentWeekRevenue.value = null
          topPerformingType.value = null
        }
      } finally {
        loading.value = false
      }
    }

    // Get current week revenue
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
        // Update selectedDate to current week
        selectedDate.value = getCurrentDate()
        analyzeTopPerformingType()
        await fetchDailyRevenue()
      } catch (error) {
        console.error('API error details:', error.response || error)

        if (error.response?.status === 401) {
          errorMessage.value = 'Authentication failed, please login again'
          hasError.value = true
        } else if (error.response?.status === 403) {
          errorMessage.value = 'Insufficient permissions, admin role required'
          hasError.value = true
        } else {
          ElMessage.error('Failed to get current week revenue: ' + (error.response?.data || error.message))
          currentWeekRevenue.value = null
          topPerformingType.value = null
        }
      } finally {
        loading.value = false
      }
    }

    // Check if selected date is current week
    const isCurrentWeek = () => {
      const today = new Date()
      const startOfWeek = new Date(today)
      // Set to Monday of this week
      startOfWeek.setDate(today.getDate() - today.getDay() + 1)
      startOfWeek.setHours(0, 0, 0, 0)

      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6)
      endOfWeek.setHours(23, 59, 59, 999)

      const selectedDateObj = new Date(selectedDate.value)

      return selectedDateObj >= startOfWeek && selectedDateObj <= endOfWeek
    }

    // Get selected week label
    const getSelectedWeekLabel = () => {
      if (!selectedDate.value) return 'Selected Week'

      const date = new Date(selectedDate.value)
      const year = date.getFullYear()

      // Calculate week number
      const firstDayOfYear = new Date(year, 0, 1)
      const pastDaysOfYear = (date - firstDayOfYear) / 86400000
      const weekNum = Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7)

      // Get start and end dates of the week
      const startOfWeek = new Date(date)
      startOfWeek.setDate(date.getDate() - date.getDay() + 1) // Set to Monday

      const endOfWeek = new Date(startOfWeek)
      endOfWeek.setDate(startOfWeek.getDate() + 6) // Set to Sunday

      const formatDate = (d) => {
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
      }

      return `Week ${weekNum}, ${year} (${formatDate(startOfWeek)} to ${formatDate(endOfWeek)})`
    }

    // Select previous week
    const selectPreviousWeek = () => {
      const date = new Date(selectedDate.value)
      date.setDate(date.getDate() - 7)
      selectedDate.value = date.toISOString().split('T')[0]
      fetchWeeklyRevenueByDate(selectedDate.value)
    }

    // Select current week
    const selectCurrentWeek = () => {
      selectedDate.value = getCurrentDate()
      fetchCurrentWeekRevenue()
    }

    // Select next week
    const selectNextWeek = () => {
      const date = new Date(selectedDate.value)
      date.setDate(date.getDate() + 7)
      selectedDate.value = date.toISOString().split('T')[0]
      fetchWeeklyRevenueByDate(selectedDate.value)
    }

    // Pie chart configuration
    const pieChartOption = computed(() => {
      if (!currentWeekRevenue.value) return {}

      return {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'horizontal',
          bottom: 0,
          left: 'center',
          data: ['Hourly Revenue', '4-Hour Revenue', 'Daily Revenue', 'Weekly Revenue']
        },
        series: [
          {
            name: 'Revenue Distribution',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '45%'],
            data: [
              {value: parseFloat(currentWeekRevenue.value.hourlyRevenue), name: 'Hourly Revenue'},
              {value: parseFloat(currentWeekRevenue.value.fourHoursRevenue), name: '4-Hour Revenue'},
              {value: parseFloat(currentWeekRevenue.value.dailyRevenue), name: 'Daily Revenue'},
              {value: parseFloat(currentWeekRevenue.value.weeklyRevenue), name: 'Weekly Revenue'}
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            label: {
              formatter: '{b}: £{c} ({d}%)'
            },
            labelLine: {
              length: 10,
              length2: 10
            }
          }
        ]
      }
    })

    // Bar chart configuration
    const barChartOption = computed(() => {
      if (!currentWeekRevenue.value) return {}

      const data = [
        {
          name: 'Hourly',
          value: parseFloat(currentWeekRevenue.value.hourlyRevenue),
          percentage: getPercentage(currentWeekRevenue.value.hourlyRevenue, currentWeekRevenue.value.totalRevenue)
        },
        {
          name: '4-Hour',
          value: parseFloat(currentWeekRevenue.value.fourHoursRevenue),
          percentage: getPercentage(currentWeekRevenue.value.fourHoursRevenue, currentWeekRevenue.value.totalRevenue)
        },
        {
          name: 'Daily',
          value: parseFloat(currentWeekRevenue.value.dailyRevenue),
          percentage: getPercentage(currentWeekRevenue.value.dailyRevenue, currentWeekRevenue.value.totalRevenue)
        },
        {
          name: 'Weekly',
          value: parseFloat(currentWeekRevenue.value.weeklyRevenue),
          percentage: getPercentage(currentWeekRevenue.value.weeklyRevenue, currentWeekRevenue.value.totalRevenue)
        }
      ]

      return {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: params => {
            const data = params[0].data
            return `${data.name}<br/>Amount: £${data.value}<br/>Percentage: ${data.percentage}%`
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLabel: {
            formatter: '£{value}'
          }
        },
        yAxis: {
          type: 'category',
          data: data.map(item => item.name)
        },
        series: [
          {
            name: 'Revenue',
            type: 'bar',
            data: data,
            label: {
              show: true,
              position: 'right',
              formatter: params => {
                return `£${params.data.value} (${params.data.percentage}%)`
              }
            },
            itemStyle: {
              color: params => {
                const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C']
                return colors[params.dataIndex]
              }
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
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
      // Ensure user is logged in and has permission
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
      barChartOption,
      dailyChartOption,
      selectedRevenueType,
      revenueTypeOptions,
      logoutAndRedirect,
      fetchCurrentWeekRevenue,
      fetchWeeklyRevenueByDate,
      getSelectedWeekLabel,
      selectPreviousWeek,
      selectCurrentWeek,
      selectNextWeek,
      getTopPerformingIcon,
      getTopPerformingClass,
      getPercentage,
      getRevenueRecommendation,
      processedDailyData,
      updateDailyChart,
      peakDays
    }
  }
}
</script>

<style scoped lang="scss">

.card-header {
  font-size: 18px;
  font-weight: bold;
}
.analysis-content {
  padding: 10px;
}
.line-chart-container {
  width: 100%;
}

.title {
  font-size: 28px;
  font-weight: bold;
  padding-left: 20px;
  padding-bottom: 20px;
  padding-top: 20px;
  border-bottom: 2px solid #003c51;
}
.card-header {
  display: flex;
  justify-content: center;
  background-color: #ffffff;
  border-bottom: 0px solid #003c51;
  align-items: center;
  font-weight: bold;
  font-size: 1.2em;
}

.card-header span{
  padding: 6px 26px;
  border: 3px solid #003c51;
  border-radius: 30px;
}

.week-selector-container {
  display: flex;
  align-items: center;
  gap: 15px;
  width: 100%;

  .custom-date-picker {
    width: 240px;
    height: 40px;

    :deep(.el-input__inner) {
      height: 40px;
      line-height: 40px;
      padding: 0 15px;
      font-size: 16px;
      font-weight: bold;
    }

    :deep(.el-input__icon) {
      line-height: 40px;
    }
  }

  .week-navigation {
    display: flex;
    gap: 12px;
    margin-left: auto;

    .nav-button {
      height: 40px;
      min-width: 140px;
      padding: 0 15px;
      font-size: 16px;
      font-weight: bold;
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;

      i {
        font-size: 14px;
        margin: 0 4px;
      }

      &.prev-button {
        background-color: #f5f7fa;
        border-color: #dcdfe6;
        color: #606266;

        &:hover {
          background-color: #e6e9ed;
          border-color: #c0c4cc;
        }
      }

      &.current-button {
        background-color: #003c51;
        border-color: #003c51;
        color: white;

        &:hover {
          background-color: #003c51;
          border-color: #003c51;
        }
      }

      &.next-button {
        background-color: #f5f7fa;
        border-color: #dcdfe6;
        color: #606266;

        &:hover {
          background-color: #e6e9ed;
          border-color: #c0c4cc;
        }
      }
    }
  }
}

@media (max-width: 992px) {
  .week-selector-container {
    flex-direction: column;
    align-items: stretch;

    .week-navigation {
      margin-left: 0;
      justify-content: space-between;

      .nav-button {
        flex: 1;
        min-width: auto;
      }
    }
  }
}

.revenue-content-container {
  display: flex;
  gap: 20px;
}

.revenue-details-container {
  flex: 1;
}

.revenue-details {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.revenue-row {
  display: flex;
  gap: 15px;
}

.revenue-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  min-width: 200px;

  &.order-count {
    background-color: #ffebee;
    border-left: 4px solid #F56C6C;
    font-weight: bold;
  }

  &.total {
    background-color: #ecf5ff;
    border-left: 4px solid #68abff;
    font-weight: bold;
  }

  &.top-performing {
    background-color: #f0f9eb;
    border-left: 4px solid #67C23A;
    font-weight: bold;
  }
}

.label {
  color: #606266;
  margin-bottom: 5px;
  font-size: 14px;
}

.value {
  font-size: 1.2em;
  color: #303133;
  font-weight: 500;
}

.chart-analysis-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  flex: 1;
}

.analysis-card {
  flex: 1;
}

.chart-container {
  height: 400px;
  width: 100%;
}

.bar-chart-container {
  height: 300px;
  width: 100%;
}

.no-data {
  padding: 40px;
  display: flex;
  justify-content: center;
}

.week-selector-card,
.current-week-card {
  margin-bottom: 20px;
}

.week-selector-card{
  margin-top: 30px;
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
  background-color: #ffffff;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 20px;
  margin-top: 10%;
  border: 1px solid #dfd782;
  min-height: 80px;
}

.highlight-icon {
  flex: 0 0 50px;
  width: 50px;
  height: 50px;
  min-width: 50px;
  min-height: 50px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  color: white;
  font-size: 24px;

  &.hourly {
    background-color: #409EFF;
  }

  &.four-hours {
    background-color: #67C23A;
  }

  &.daily {
    background-color: #E6A23C;
  }

  &.weekly {
    background-color: #F56C6C;
  }

  &.default {
    background-color: #909399;
  }
}

.highlight-text {
  flex: 1;
  min-width: 0;
}

.highlight-text h3 {
  margin: 0;
  color: #303133;
  font-size: 16px;
}

.highlight-text p {
  margin: 5px 0 0;
  color: #606266;
  font-size: 14px;
}

.analysis-recommendation {
  flex: 1;
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  border: 4px solid #5ba4f8;
  max-width: 400px;

  h3 {
    margin-top: 0;
    color: #303133;
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  h4 {
    margin-top: 0;
    color: #303133;
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  p {
    margin-bottom: 0;
    color: #606266;
    font-size: 18px;
    line-height: 1.6;
  }
}

.peak-days-analysis {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f8f8;
  border-radius: 4px;
  border: 1px solid #093159;
}

.peak-day-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.peak-label {
  font-weight: 500;
  color: #606266;
}

.peak-value {
  font-weight: 600;
  color: #093159;
}

.analysis-recommendation {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

@media (max-width: 1200px) {
  .revenue-content-container {
    flex-direction: column;
  }

  .analysis-recommendation {
    max-width: 100%;
    margin-top: 20px;
  }
}

@media (max-width: 768px) {
  .chart-analysis-container {
    flex-direction: column;
  }

  .revenue-row {
    flex-direction: column;
    gap: 10px;
  }

  .revenue-item {
    min-width: auto;
  }

  .week-navigation {
    flex-wrap: wrap;

    .nav-button {
      min-width: calc(50% - 6px);
      margin-bottom: 12px;
    }
  }

}

@media (max-width: 480px) {
  .week-navigation {
    .nav-button {
      min-width: 100%;
    }
  }

  .title {
    font-size: 22px;
    padding: 15px 10px;
  }

  .card-header {
    font-size: 1em;
  }

  .week-selector-container .custom-date-picker {
    width: 100%;
  }

  .analysis-recommendation {
    padding: 15px;

    h4 {
      font-size: 18px;
    }

    p {
      font-size: 16px;
    }
  }
}
</style>