<template>
  <div>
      <div style="margin-top: 20px;">
          <!-- 搜索 -->
          <el-input size="small" v-model="input_name" placeholder="请输入邮件名称，可回车搜索..." prefix-icon="el-icon-search"
          style="width: 400px;margin-right: 10px;" @keydown.enter.native="search_name"></el-input>
          <el-button size="small" type="primary"  @click="search_name" icon="el-icon-search">搜索</el-button>
      </div>
      <div>
          <el-table
          :data="emailData"
          style="width: 90%">
          <el-table-column
            label="编号"
            width="230">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.id }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="邮件标题"
            width="230">
            <template slot-scope="scope">
              <el-tag size="medium">{{ scope.row.subject }}</el-tag>
            </template>
          </el-table-column>
            <el-table-column
                label="邮箱"
                width="230">
              <template slot-scope="scope">
                <el-tag size="medium">{{ scope.row.toAddress }}</el-tag>
              </template>
            </el-table-column>

          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                  size="mini"
                  type="primary"
                  @click="viewDetails(scope.row)">查看</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

    <!--邮件详情-->
    <el-dialog
        title="邮件详情"
        :visible.sync="dialog_edit"
        width="60%"
        heigh="500px"
    >
      <el-form >
        <quill-editor v-model="mailDetails"  style="height: 400px;">
        </quill-editor>
      </el-form>
      <span slot="footer">
          <el-button @click="dialog_edit=false">关闭</el-button>
        </span>
    </el-dialog>

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
import {
  quillEditor
} from 'vue-quill-editor'

export default {
  name: 'CategoryBlog',
  inject:['reload'],
  data () {
   return {
     emailData:[],  //分类数据
     currentPage: 1,  //当前页
     total:0, //总记录数
     pagesize:5, //页面大小
     pagesizes:[5,10,15], //页面数组
     input_name:'', //搜索框值
     mailDetails:'',
      showPage: true, //是否显示分页
     dialog_edit:false
   }
  },
  components: {
    quillEditor
  },
  mounted() {
    this.initType();
  },
  methods:{
    //初始化分类数据
    initType(){
      const _this = this
      this.$axios.get('/email/getEmailByPage?current=' + this.currentPage + '&size=' + this.pagesize).then(resp=>{
        console.log(resp.data)
        _this.emailData = resp.data.data.records
        _this.total = resp.data.data.total
      })
    },
    //查看邮件详情
    viewDetails(row){
      this.dialog_edit=true
      this.mailDetails=row.content
    },
    //删除邮件
    handleDelete(index, row) {
      const _this = this
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.delete('/email/deleteEmailById?id=' + row.id).then(resp=>{
            if(resp){
              this.initType()
            }
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

    //搜索邮件
    search_name(){
       const _this = this
       this.getRequest('/type/getTypeByName?name=' + this.input_name).then(resp=>{
         console.log(resp)
         if(_this.input_name == ''){
           _this.initType();
           _this.showPage = true
         }else{
          _this.typeData = resp.obj
          _this.showPage = false
          _this.input_name = ''
         }


       })
    },    // 分页的当前页
    handleCurrentChange(val) {
      this.currentPage = val
      this.initType()
    },
    //每页多少条
    handleSizeChange(val) {
      this.pagesize = val
      this.initType()
    },
  }
}
</script>

<style scoped>

</style>
