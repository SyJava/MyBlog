<template>
	<div id="app">
    <!--    isRouterAlive控制页面刷新-->
    <router-view v-if="isRouterAlive"/>
	</div>
</template>

<script>
	export default {
		name: "App",
    provide(){
    return{
    reload:this.reload
    }
    },
    data(){
		  return{
		    isRouterAlive:true
      }
    },
    methods:{
		  reload(){
		    this.isRouterAlive=false
         this.$nextTick(function (){
           this.isRouterAlive=true
         })
      }
    },
		watch: {
			$route(to, from) {

				console.log("to")
				console.log(to)

				if (to.path != '/login') {
					let obj = {
						name: to.name,
						title: to.meta.title
					}
				}

			}
		}
	}

</script>

<style>
	html, body, #app {
		font-family: 'Helvetica Neue', 'Hiragino Sans GB', 'WenQuanYi Micro Hei', 'Microsoft Yahei', sans-serif;
		height: 100%;
		padding: 0;
		margin: 0;
		font-size: 15px;
	}
</style>
