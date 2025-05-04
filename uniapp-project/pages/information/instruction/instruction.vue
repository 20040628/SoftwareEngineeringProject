<template>
	<view>
		<uni-section title="纵向排列" type="line" padding>
			<uni-steps :options="list2" active-color="#007AFF" :active="active" direction="column" />
		</uni-section>
	</view>
</template>


<script>
	export default {
		components: {},
		data() {
			return {
				active: 1,
				list2: [{
					title: '注册，且有卡',
					desc: '1'
				}, {
					title: '选择租车地址和时间',
					desc: '1'
				}, {
					title: '确认租的车辆',
					desc: '2018-11-13'
				}, {
					title: '支付',
					desc: '2018-11-14'
				},{
					title: '售后',
					desc: '2018-11-14'
				}
				]
			}
		},
		methods: {
			
		}
	}
</script>



<style lang="scss">

	.example-body {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		padding: 15px;
		flex-direction: row;
	}
</style>

