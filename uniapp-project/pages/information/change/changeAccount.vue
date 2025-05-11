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
		                        name: "Reset Password",
		                        url:"../../../pages/information/resetPassword/resetPassword"
		                    },
		                ]
		            }
		        },
	}
</script>

<style>
</style>