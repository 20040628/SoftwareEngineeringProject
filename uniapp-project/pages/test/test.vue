<template>
  <view>
    <map :latitude="latitude" :longitude="longitude" :scale="14" :markers="markers"></map>
  </view>
</template>

<script>
export default {
  data() {
    return {
      latitude: 40.7128,
      longitude: -74.0060,
      scale: 14, // 设置地图的缩放级别
      markers: [ // 设置标记点
        {
          latitude: 40.7128,
          longitude: -74.0060,
          title: 'New York City',
          iconPath: '/static/marker.png',
        },
      ],
    };
  },
};
</script>
