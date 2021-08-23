<template>
    <div class="m_container">

      <!-- 博客内容 -->
      <div class="m_content">
        <el-form ref="editForm" status-icon :model="editForm" :rules="rules1"  label-width="80px">
          <el-form-item label="标题" prop="title">
            <el-input v-model="editForm.title"></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input v-model="editForm.description"></el-input>
          </el-form-item>
          <el-form-item label="首页图片" prop="firstPicture">
            <el-input v-model="editForm.firstPicture" clearable></el-input>
          </el-form-item>

          <el-form-item label="文章标签">
            <el-tag
                v-show="tag.tagName!=null"
              :key="index"
              v-for="(tag,index) in editForm.tags"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)">
              {{tag.tagName}}
            </el-tag>
            <el-input
              style="width: 30%;margin-left: 10px;"
              v-if="inputVisible"
              v-model="inputValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleInputConfirm"
              @blur="handleInputConfirm"
            >
            </el-input>
            <el-button v-else class="button-new-tag" style="margin-left: 10px;" size="small" @click="showInput">+ New Tag</el-button>
          </el-form-item>

          <el-form-item label="分类专栏" prop="typeId">
            <el-select v-model="editForm.typeId" placeholder="请选择分类专栏">
              <el-option v-for="(item,index) in getalltype" :key="item.index" :label="item.name" :value="item.id + ''"></el-option>
            </el-select>
            <el-button type="primary" size="small" @click="dialog2= true" style="margin-left: 10px;">新建分类专栏</el-button>
          </el-form-item>
          <el-form-item label="文章类型" prop="isOriginal">
            <el-select v-model="editForm.isOriginal" placeholder="请选择文章类型，默认为原创">
              <el-option label="原创" value="true"></el-option>
              <el-option label="转载" value="false"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="发布形式" prop="isPublished">
            <el-select v-model="editForm.isPublished" placeholder="请选择发布形式，默认为私密">
              <el-option label="公开" value="true"></el-option>
              <el-option label="私密" value="false"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="内容" prop="content">
            <mavon-editor @imgAdd="$imgAdd" @imgDel="$imgDel" ref=md v-model="editForm.content"/>
          </el-form-item>
          <el-form-item style="margin: auto;">
            <el-button type="primary" @click="saveForm('editForm')">保存草稿</el-button>
            <el-button type="primary" @click="submitForm('editForm')">发布博客</el-button>
            <router-link to="/admin/allblogs">
              <el-button style="margin-left: 10px;">返回</el-button>
            </router-link>
          </el-form-item>
        </el-form>
      </div>


      <!-- 新建分类专栏 -->
      <el-dialog
        title="新建分类专栏"
        :visible.sync="dialog2"
        width="30%"
        >
        <el-form status-icon :model="type" ref="type" :rules="rules2" label-width="120px">
          <el-form-item label="分类专栏名" prop="name">
            <el-input v-model="type.name" placeholder="请输入要新建的分类专栏名"></el-input>
          </el-form-item>
        </el-form>
         <span slot="footer" class="dialog-footer">
            <el-button @click="dialog2 = false">取 消</el-button>
            <el-button type="primary" @click="submitForm('type')">确 定</el-button>
          </span>
      </el-dialog>


    </div>
  </template>
  <script>
    export default {
      name: "EditBlog",
      data() {
        return {
          // id:this.$route.params.id,
          editForm: {  //博客文章表单
            blogId: null,
            title: '',
            description: '',
            firstPicture: '',
            content: '',
            typeId: '',
            isOriginal:'',
            isPublished: '',
            shareStatement:'',
            tags: [
              {
                tagId:0,
                tagName:null
              }
            ],
          },
          type:{  //分类专栏
             name:''
          },
          getalltype:[], // 所有分类专栏
          dialogFormVisible: false,  //控制发布博客对话框
          dialog2: false,  //控制新增分类专栏对话框
          rules1: {  //表单校验
            title: [
              {required: true, message: '请输入标题', trigger: 'blur'},
              {min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur'}
            ],
            description: [
              {required: true, message: '请输入摘要', trigger: 'blur'}
            ],
            firstPicture: [
              {required: true, message: '请输入图片地址', trigger: 'blur'}
            ],
            content: [
              {required: true, message: '请输入文章内容', trigger: 'blur'}
            ],
            typeId: [
              {required: true, message: '分类专栏不能为空', trigger: 'blur'}
            ],
            isOriginal: [
              {required: true, message: '文章类型不能为空', trigger: 'blur'}
            ],
            isPublished: [
              {required: true, message: '发布形式不能为空', trigger: 'blur'}
            ],
          },
          rules2:{ //表单校验
            name: [
              {required: true, message: '分类专栏名不能为空', trigger: 'blur'}
            ],
          },
          formLabelWidth: '120px' , // 输入框的宽度
          inputVisible: false,
          inputValue:'',
        }
      },
      props:['id'],
      mounted() {
        this.initBlog()
        this.initType()
      },
      methods: {
        //删除标签
        handleClose(tag) {
          this.editForm.tags.splice(this.editForm.tags.indexOf(tag), 1);
          if(tag.tagId!=null&&tag.tagId!='') {//如果是从后端传过来的标签，而不是在编辑时新加的
            this.$axios.delete('/tag/deleteTagById?id=' + tag.tagId).then(resp => {
              console.log(resp.data)
            })
          }
        },
        //输入标签
        showInput() {
          this.inputVisible = true;
          this.$nextTick(_ => {
            this.$refs.saveTagInput.$refs.input.focus();
          });
          console.log(this.editForm.tags)
        },
        //回车添加标签
        handleInputConfirm() {
          const inputValue = this.inputValue;
          if (inputValue) {
            this.editForm.tags.push({tagName:inputValue});
          }
          this.inputVisible = false;
          this.inputValue = '';
        },

        //初始化博客数据
        initBlog(){
          const _this = this
          this.$axios.get('/blog/getByBlogId?id=' + this.id).then(resp=>{
              console.log(resp.data)
              _this.editForm = resp.data.data
              _this.editForm.isPublished = resp.data.data.isPublished? "公开":"私密"
              _this.editForm.isOriginal = resp.data.data.isOriginal? "原创":"转载"
              _this.editForm.typeId = resp.data.data.typeId+''//空格不能删
          })
        },
        //初始化文章专栏
        initType(){
          const _this = this
          this.$axios.get('/type/getAllType').then(resp=>{
            console.log(resp)
            _this.getalltype = resp.data.data
          })
        },
        //添加新的分类专栏
        addNewType(){
          const _this = this
          this.$axios.post('/type/addType',this.type).then(resp=>{
            if(resp){
              _this.type.name = ''
              this.initType()
              _this.dialog2 = false
            }
          })
        },

        //校验博客基本内容表单
        submitForm(formName) {
          const _this = this
          this.$refs[formName].validate((valid) => {
            if (valid) {
              //转为布尔类型
              if(_this.editForm.isPublished == "公开"){
                _this.$set(_this.editForm, 'isPublished', true)
              }else if (_this.editForm.isPublished == "私密"){
                _this.$set(_this.editForm, 'isPublished', false)
              }
              if(_this.editForm.isOriginal == "原创"){
                _this.$set(_this.editForm, 'isOriginal', true)
              }else if (_this.editForm.isOriginal == "转载"){
                _this.$set(_this.editForm, 'isOriginal', false)
              }
              //发布博客
                this.$axios.put('/blog/updateBlog',this.editForm).then(resp=>{
                    if(resp){
                      _this.$message.success(resp.data.msg)
                      _this.$router.push('/admin/allblogs')
                    }
                })
            } else {
              return false;
            }
          });
        },
//保存草稿
        saveForm(formName) {
          const _this = this
          this.$refs[formName].validate((valid) => {
            if (valid) {
              //转为布尔类型
              if(_this.editForm.isPublished == "公开"){
                _this.$set(_this.editForm, 'isPublished', true)
              }else if (_this.editForm.isPublished == "私密"){
                _this.$set(_this.editForm, 'isPublished', false)
              }
              if(_this.editForm.isOriginal == "原创"){
                _this.$set(_this.editForm, 'isOriginal', true)
              }else if (_this.editForm.isOriginal == "转载"){
                _this.$set(_this.editForm, 'isOriginal', false)
              }
              //发布博客
              this.$axios.put('/blog/temporarySave',this.editForm).then(resp=>{
                if(resp){
                  this.$message.success(resp.data.msg)
                  _this.$router.push('/admin/allblogs')
                }
              })
            } else {
              return false;
            }
          });
        },
        //上传图片
        // 绑定@imgAdd event
        $imgAdd(pos, $file){
          var _this = this
          // 第一步.将图片上传到服务器.
          var formdata = new FormData();
          formdata.append('image', $file);
          this.$axios.post('/uploadImg',formdata).then((resp) => {
            // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
            /**
             * $vm 指为mavonEditor实例，可以通过如下两种方式获取
             * 1. 通过引入对象获取: `import {mavonEditor} from ...` 等方式引入后，`$vm`为`mavonEditor`
             * 2. 通过$refs获取: html声明ref : `<mavon-editor ref=md ></mavon-editor>，`$vm`为 `this.$refs.md`
             */
            //  $vm.$img2Url(pos, url);
            console.log(resp)
            if(resp.status === 200){
              let url = resp.data.data
              _this.$refs.md.$img2Url(pos,url)
            }
          })
        },
        //删除图片
        $imgDel(pos) {
          const imgPath=pos[0]
          console.log(imgPath)
          this.$axios.delete('/delImg?imgPath='+imgPath).then((resp) => {//使用delete请求
            console.log("删除成功")
          })
        }

      }
    }
  </script>

  <style>
    .m_container{
      margin-top: 20px;
    }
  </style>
