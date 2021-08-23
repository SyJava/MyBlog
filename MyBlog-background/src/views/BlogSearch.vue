<template>
    <div style="height: 100vh;overflow-x: hidden;" class="wrapper">
        <!-- 回到顶部 -->
        <el-backtop target=".wrapper">
          <i class="el-icon-caret-top"></i>
        </el-backtop>
        <Head></Head>

        <!-- 搜索框 -->
        <div style="width: 50%;margin: auto;margin-top: 3%;">
          <div style="background-color: #bac3e9;padding: 2%;">
            <el-input size="small"
              placeholder="请输入内容"
              v-model="input"
              ref="ipt"
              clearable
              @keyup.enter.native="blogsearch(input)">
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
          </div>
        </div>

        <!-- 内容区 -->
          <div>
              <div v-model="blogData" class="grid-content content bg-purple blog_bg ">

                  <el-row :gutter="20">
                      <el-col :span="12"><div style="margin: 15px 0px 15px 20px;color: blueviolet;">文章</div></el-col>
                      <el-col :span="12"><div style="float: right;margin: 10px 20px 0px 0px;">共
                          <span style="font-size: 28px;color: rgb(6, 154, 240);">{{total}}</span> 篇</div></el-col>
                    </el-row>


                   <!-- 分割线 -->
                   <div  style="height:3px; margin: 5px 0; border:none; border-top:3px solid  #1eb9e0;"></div>

                <!-- 文章 -->
                <div>
                  <div style=" width: 95%; margin: 2%;" v-for="(item,index) in blogData" :key="index" >

                  <router-link :to="'/blog/'+item.blogId">
                   <el-card shadow="hover" :body-style="{ padding: '15px' }" >
                     <el-row :gutter="20">
                       <el-col :span="17">
                         <div>
                           <p v-html="item.title"></p>
                            <p style="font-size: 17px;"v-html="item.description"></p>

                            <div>
                             <el-avatar style="vertical-align:middle" :src="item.user.avatar"></el-avatar>
                             <span style="margin-left: 5px;">{{item.user.username}}</span>
                             <i style="margin-left: 20px;" class="el-icon-date"><span style="font-size: 18px;"> {{item.createTime}}</span></i>
                             <i style="margin-left: 20px;" class="el-icon-view"><span style="font-size: 18px;"> {{item.views}}</span></i>
                             <i style="margin-left: 20px;" class="el-icon-chat-line-square"><span style="font-size: 18px;"> {{item.commentCount}}</span></i>
                            </div>

                         </div>
                       </el-col>
                       <el-col :span="7">
                         <div>
                           <el-image
                             style="width: 100%; height: 100%"
                             :src="item.firstPicture"
                             >
                           </el-image>
                           <div style="margin-bottom: 10px;">
                             <el-tag style="margin-right: 10px;" ><span> {{item.isOriginal?'原创':'转载'}}</span></el-tag>
                             <el-tag style="margin-right: 10px;" type="success"><span > {{item.type.name}}</span></el-tag>
                             <el-tag style="margin-right: 10px;" type="info" v-for="tag in item.tags"><span> {{tag.tagName}}</span></el-tag>

                           </div>
                         </div>
                       </el-col>
                     </el-row>

                   </el-card>
                  </router-link>

                   <el-divider></el-divider>

                  </div>


               </div>



              </div>
          </div>

        <Foot></Foot>
    </div>
  </template>

  <script>
  import Head from '@/components/Head'
  import Foot from '@/components/Foot'
  export default {
    name: 'BlogType',
    components:{ Head,Foot },
    data () {
     return {
         types:[],
         type_id:'',
         blogData:[],
         total:0,
         currentPage:1,
         size:5,
         searchkey:'',
         input:''
     }
    },
    mounted() {
      this.$refs.ipt.focus()
    },
    methods:{
      //搜索博客
      blogsearch(input){
        const _this = this
        this.axios.get('/blog/search?value=' + input ).then(resp=>{
          console.log(resp)
          _this.blogData = resp.data.data
          _this.total = resp.data.data.length
          if(_this.total > 0){
          this.$message({
            message: '查询成功！',
            type: 'success'
          });
        }else{
          this.$message.error('抱歉，没有找到相关文章，请重新搜索~');
        }
        })
      }


    }
  }
  </script>

  <style scoped>
      .blog_bg{
          box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
          width: 60%;
          margin-top: 3% !important;
          margin-bottom: 5% !important;
          margin: auto;
          min-height: 500px;
      }
  </style>
