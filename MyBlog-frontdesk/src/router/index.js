import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import UpdatePass from "@/views/UpdatePass.vue";
import WriteBlog from "@/views/blogs/WriteBlog";
import EditBlog from "@/views/blogs/EditBlog";
import AllBlogs from "@/views/blogs/AllBlogs";
import CategoryBlog from "@/views/blogs/CategoryBlog";
import TagBlog from "@/views/blogs/TagBlog";
import CommentBlog from '../views/blogs/CommentBlog'
import LinksBlog from '../views/blogs/LinksBlog'
import email from "@/components/emali/email";
import Email from "@/views/blogs/Email";
Vue.use(VueRouter)

const routes = [

	{
		path: '/',
		component: Home,
		name: '博客管理',
		children:[
			{ path: '/admin/writeblog', name: '写博客',  component: WriteBlog },
			{ path: '/admin/editblog/:id', name: '编辑博客',  component: EditBlog, props: true, hidden: true },
			{ path: '/admin/allblogs', name: '文章管理',  component: AllBlogs  },
			{ path: '/admin/categoryblog', name: '分类专栏',  component: CategoryBlog  },
			{ path: '/admin/tagblog', name: '标签管理',  component: TagBlog  },
			{ path: '/admin/commentblog', name: '评论管理',  component: CommentBlog  },
			{ path: '/admin/email', name: '邮件管理',  component: Email  },
			{ path: '/admin/linksblog', name: '友链管理',  component: LinksBlog  },

		]
	},
	{
		path: '/login',
		name: 'Login',
		component: () => import('@/views/Login.vue'),
		hidden:true
	},
	{
		path: '/UpdatePass',
		name: 'UpdatePass',
		component: UpdatePass,
		hidden:true
	},
	{
		path: '/email',
		name: 'email',
		component: email,
		hidden:true
	}
]

const router = new VueRouter({
	routes
})

router.beforeEach((to, from, next) => {

	let token = localStorage.getItem("token")

	if (to.path == '/login') {
		localStorage.removeItem("token")//跳转到登录页面时，删除jwt
		next()

	} else if (!token) {//jwt为空，跳转到登录页面
		next({path: '/login'})
	}
	next()
})



export default router
