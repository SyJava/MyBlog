<template>
   <div class="body">
     <div class="top_div"></div>
     <div style="background: #008ead; margin: auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 1000px; height: 224px; text-align: center;">
       <div style="width: 165px; height: 96px; position: absolute;">
         <div class="tou"></div>
         <div class="initial_left_hand" id="left_hand"></div>
         <div class="initial_right_hand" id="right_hand"></div>
       </div>
       <p style="padding: 30px 0px 10px; position: relative;"><span
         class="u_logo"></span>
         <input id="loginName" class="ipt" type="email" v-model="email" placeholder="请输入邮箱地址" value="">
         <input class="ipt" id="password" type="text" v-model="subject" placeholder="请输入邮件标题" value="">
       </p>

       <el-card style="height: 450px;">
         <quill-editor v-model="content" ref="myQuillEditor" style="height: 500px;" :options="editorOption">
         </quill-editor>
       </el-card>
       <div style="height: 50px; line-height: 50px; margin-top: -4px;background:#fff; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">

         <span style="float: right;">
              <a id="loginBtn" @click="send()">发送</a>
           </span></div>

     </div>

   </div>
</template>

<script>
import {
  quillEditor
} from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
export default {
  name: 'HelloWorld',
  data () {
    return {
      email:'',
      subject: '',
      content:'',
      editorOption: {}
    }
  },
  components: {
    quillEditor
  },
  created() {
    this.getEmail()
  },
  methods:{
    //获取邮箱地址
    getEmail(){
      this.email = this.$route.query.email;
    },

    send(){
      //验证邮箱
      var myReg=/^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;

      if(myReg.test(this.email)){
        this.$axios.post("/email/sendMail", {

          "email":this.email,
          "subject":this.subject,
          "content":this.content
        }).then( res => {
          this.$message.success(res.data.msg)
        }).catch( res => {
          this.$message.failure(res.data.msg)

        })
      }else {
        alert("邮箱格式不对!");
      }

    }

  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

  *{padding: 0px;margin: 0px;}
  .body{
    background: #008ead;
    width: 100%;
    height: 100%;
  }
  .top_div{
    background: #008ead;
    width: 100%;
    height: 160px;
  }
  .ipt{
    border: 1px solid #d3d3d3;
    padding: 10px 10px;
    width: 420px;
    border-radius: 4px;
    padding-left: 35px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s
  }
  .ipt:focus{
    border-color: #66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6)
  }
  .u_logo{
    background: url("../../assets/images/username.png") no-repeat;
    padding: 10px 10px;
    position: absolute;
    top: 43px;
    left: 40px;

  }
  .p_logo{
    background: url("../../assets/images/password.png") no-repeat;
    padding: 10px 10px;
    position: absolute;
    top: 12px;
    left: 40px;
  }
  a{
    text-decoration: none;
  }
  .tou{
    background: url("../../assets/images/tou.png") no-repeat;
    width: 97px;
    height: 92px;
    position: absolute;
    top: -87px;
    left: 140px;
  }
  .left_hand{
    background: url("../../assets/images/left_hand.png") no-repeat;
    width: 32px;
    height: 37px;
    position: absolute;
    top: -38px;
    left: 150px;
  }
  .right_hand{
    background: url("../../assets/images/right_hand.png") no-repeat;
    width: 32px;
    height: 37px;
    position: absolute;
    top: -38px;
    right: -64px;
  }
  .initial_left_hand{
    background: url("../../assets/images/hand.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -12px;
    left: 100px;
  }
  .initial_right_hand{
    background: url("../../assets/images/hand.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -12px;
    right: -112px;
  }
  .left_handing{
    background: url("../../assets/images/left-handing.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -24px;
    left: 139px;
  }
  .right_handinging{
    background: url("../../assets/images/right_handing.png") no-repeat;
    width: 30px;
    height: 20px;
    position: absolute;
    top: -21px;
    left: 210px;
  }
  #loginBtn {
    margin-right: 30px;
    background: rgb(0, 142, 173);
    padding: 7px 10px;
    border-radius: 4px;
    border: 1px solid rgb(26, 117, 152);
    border-image: none;
    color: rgb(255, 255, 255);
    font-weight: bold;
    cursor: pointer;
  }
</style>
