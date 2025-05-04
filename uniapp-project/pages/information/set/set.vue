<template>
	<set-list :list="list"></set-list>
</template>

<script>
	import SetList  from '../../../components/set-list/set-list.vue'
	export default {
		 components: {
		    SetList
		  },
		data() {
		            return {
		                list: [{
		                        name: "Account and Security",
		                        url:"../../../pages/information/change/changeAccount"
		                    },
		                    {
		                        name: "Modify personal information",
		                        url:"../../../pages/information/change/changeInfo"
		                    },
		                    {
		                        name: "About APP",
		                        url:"../../../pages/information/app/app"
		                    },
		                ]
		            }
		        },
	}
</script>

<style>
</style>