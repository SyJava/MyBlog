<template>
    <div>
        <el-container>
            <el-header class="homeHeader">
                <a href="/#/"><div class="title">SY个人博客后台</div></a>
                <el-dropdown class="userInfo" @command="commandHandler">  <!--@command="commandHandler" 点击菜单项触发的事件回调-->
                    <span class="el-dropdown-link">
                        {{user.username}}
                        <i><img :src="user.avatar" alt=""></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="userInfo"><router-link to="/UpdatePass">修改密码</router-link></el-dropdown-item>
                         <!--disabled:禁用的    divided:有分隔线-->
                        <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-header>
            <el-container>
                <el-aside width="200px">

                    <el-menu router>
                         <el-menu-item index="/">
                            <i class="el-icon-menu"></i>
                            <span slot="title">首页</span>
                          </el-menu-item>
                         <!--这个遍历拿到的是index.js里面的routers地址数组 -->
                         <!-- !item.hidden 是将home和login的路由名隐藏，不需要在左侧菜单渲染出来 -->
                        <el-submenu :index="item.path" v-for="(item,index) in this.$router.options.routes" v-if="!item.hidden" :key="index">
                            <template slot="title">
                                <i class="el-icon-location"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item :index="child.path" v-for="(child,indexj) in item.children" v-if="!child.hidden" :key="indexj">
                                {{child.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>

                <el-main>
                    <!-- 面包屑 -->
                    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="this.$router.currentRoute.path!='/'">
                        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/'">
                      终于登上了！
                    </div>

                    <router-view/>
                </el-main>

            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data(){
            return{
                user:JSON.parse(localStorage.getItem("user"))//将字符串转为json格式
            }
        },
        methods:{
            commandHandler(cmd){  //该方法有一个参数，cmd
                if(cmd=='logout'){
                    this.$confirm('此操作将注销登录, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                      this.$axios.post("/logout").then(res => {
                        localStorage.clear()
                        this.$router.push("/login")
                      })

                        }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消注销'
                            });
                        });

                }
            }
        },
      created() {
        // console.log(localStorage.getItem(user))
      }
    }

</script>

<style>
    .homeHeader{
        background-color:#409eff;
        display: flex;
        align-items: center; /*竖轴上居中*/
        justify-content:space-between; /*空白的地方在中间*/
        padding: 0 15px;
        box-sizing: border-box;
    }
    /* 字体样式 */
    .title{
        font-size: 30px;
        font-family: 华文行楷;
        color: #ffffff;
    }
    /*设置鼠标移上去显示为手指*/
    .userInfo{
        cursor: pointer;
    }
    /* 头像样式 */
    .el-dropdown-link img{
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }
    /* 用户名样式 */
    .el-dropdown-link{
        display: flex;
        align-items: center;
    }
    /* 面包屑样式 */
    .homeWelcome{
        text-align: center;
        font-size: 30px;
        font-family: 华文行楷;
        color: #409eff;
        padding-top: 50px;
    }
    a{
        text-decoration:none;
    }


</style>
