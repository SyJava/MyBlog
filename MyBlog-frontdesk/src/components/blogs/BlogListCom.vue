<template>
  <div>
        <!-- 博客文章 -->
        <el-table
            v-loading="loading"
            element-loading-text="正在加载..."
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :data="blogsData"
            stripe
            style="width: 100%">
            <el-table-column label="文章列表">
            <template slot-scope="scope">
                <el-card  class="box-card">
                <div style="font-size: 18px;">
                    <!-- v-if="!scope.row.shareStatement" 这里根据shareStatement属性判断文章是否为草稿 -->
                    <el-button size="mini" v-if="!scope.row.shareStatement" type="info" icon="el-icon-edit" circle></el-button>
                    {{scope.row.title}}
                </div>
                <div style="margin-top: 10px;">

                    <el-tag size="small" style="margin-right: 10px;"  v-if="scope.row.isDelete" type="danger">已删除</el-tag>
                    <el-tag size="small" style="margin-right: 10px;"  v-if="scope.row.shareStatement" type="warning">草稿</el-tag>
                    <el-tag style="margin-right: 10px;" v-if="!scope.row.shareStatement" size="small">{{scope.row.isOriginal ? '原创':'转载'}}</el-tag>
                    <el-tag style="margin-right: 20px;" v-if="!scope.row.shareStatement" size="small" type="info">{{scope.row.isPublished  ? '公开' : '私密'}}</el-tag>
<!--                     <el-tag style="margin-right: 20px;" v-if="scope.row.shareStatement" size="small" type="success">{{scope.row.typeId}}</el-tag>-->
                    <i style="margin-right: 20px;" class="el-icon-view"> {{scope.row.views}} </i>
                    <i style="margin-right: 20px;" class="el-icon-chat-square"> {{scope.row.commentCount}} </i>
                    <i style="margin-right: 20px;" class="el-icon-date"> {{scope.row.createTime}}</i>

                    <el-button v-if="!scope.row.isDelete" style="float: right;" type="danger" size="mini" @click="logicDeleteBlog(scope.row.blogId)">删除</el-button>
                    <router-link :to=" '/admin/editblog/' + scope.row.blogId ">
                      <el-button v-if="!scope.row.isDelete" style="float: right; margin-right: 10px;" type="primary" size="mini">编辑</el-button>
                    </router-link>
                    <el-button v-if="scope.row.isDelete" style="float: right;" type="danger" size="mini" @click="deleteBlog(scope.row.blogId)">删除</el-button>
                    <el-button v-if="scope.row.isDelete" style="float: right; margin-right: 10px;" @click="recoveryBlog(scope.row.blogId)" type="primary" size="mini">还原</el-button>


                </div>

                </el-card>
            </template>

            </el-table-column>
        </el-table>

       <!-- 分页 -->
       <div style="margin-top: 20px;">
              <el-pagination
                background
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="pagesizes"
                :page-size="pagesize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
              </el-pagination>
       </div>

  </div>
</template>

<script>
export default {
  name: 'BlogListCom',
  inject:['reload'],
  data () {
   return {
    blogsData: [], //文章数据
    currentPage: 1,  //当前页
    total:0, //总记录数
    pagesize:10, //页面大小
    pagesizes:[10,20,30], //页面数组
    t2index:0, //选项卡index
    loading:false, //加载样式
   }
  },
  props:["tindex"],
  mounted() {
    this.initIndex();
    this.initBlogs(); // 调用初始化博客数据

  },

  methods:{
    //删除博客(逻辑删除)
    logicDeleteBlog(id){
      const _this = this
      this.$confirm('此操作将删除该文章, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.delete('/blog/logicDeleteBlog?id=' + id).then(resp=>{
            _this.$message.success("删除成功")
            this.reload() // 刷新当前页面
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },
    //彻底删除博客
    deleteBlog(id){
      const _this = this

      this.$confirm('此操作将彻底删除该文章, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.delete('/blog/deleteBlog?id=' + id).then(resp=>{
            _this.$message.success(resp.data.msg)
            this.reload() // 刷新当前页面
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },
    //还原博客
    recoveryBlog(id){
      const _this = this
      this.$confirm('确定要还原该博客吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.post('/blog/recoveryBlog?id=' + id).then(resp=>{
            _this.$message.success(resp.data.msg)
            this.reload() // 刷新当前页面
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消还原'
          });
        });
    },
    //组件的index
    initIndex(){
        this.t2index = this.tindex  //保存父组件传过来的值
    },
    // 初始化【全部】博客的数据
    initBlogs(){
      const _this = this
      // 通用路由
      var baseurl = '/blog/getByPage?current=' + this.currentPage + '&size=' + this.pagesize
      //通过条件拼接路由
      if(this.t2index == "0"){ //全部
        baseurl +=' &isDelete=0'
      }
      if(this.t2index == "1"){ //原创
        baseurl +=' &isOriginal=1 &shareStatement=0  &isDelete=0'
      }
      if(this.t2index == "2"){ //转载
        baseurl +=' &isOriginal=0 &shareStatement=0 &isDelete=0'
      }
      if(this.t2index == "3"){  //草稿
        baseurl +=' &shareStatement=1  &isDelete=0'
      }
      if(this.t2index == "4"){ //公开
        baseurl +=' &isPublished=1 &shareStatement=0  &isDelete=0'
      }
      if(this.t2index == "5"){ //私密
        baseurl +=' &isPublished=0 &shareStatement=0 &isDelete=0'
      }
      if(this.t2index == "6"){ //回收站
        baseurl +=' &isDelete=1'
      }
      // console.log(baseurl)
      //显示加载样式
      this.loading = true
      this.$axios.get(baseurl).then(resp=>{
        // console.log(baseurl)
        if(resp){
          console.log(resp)
          console.log(resp.data.data)
          //隐藏加载样式
          _this.loading = false
          _this.blogsData = resp.data.data.records  //将获取到的后端的值赋值给blogsData
          console.log(_this.blogsData)
          _this.total = resp.data.data.total   // 保存一下总记录数，用于前端展示
        }
      })
    },
    // 分页的当前页
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage = val
      this.initBlogs()
    },
    //每页多少条
    handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pagesize = val
        this.initBlogs()
    },

  }
}
</script>

<style scoped>

</style>
