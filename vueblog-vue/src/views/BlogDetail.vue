<template>
  <div>
    <Header></Header>
    <div class="mblog">
      <h2>{{ blog.title }}</h2>
      <el-link icon="el-icon-edit" v-if="ownBlog">
        <!--   name传组件名  params传递参数,传小写  需要对象：v-bind 绑定对象   -->
        <router-link :to="{name:'BlogEdit',params:{blogid:blog.id}}"></router-link>
      </el-link>
      <el-divider></el-divider>
      <div class="markdown-body" v-html="blog.content"></div>

    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";
import 'github-markdown-css'

export default {
  name: "BlogDetail",
  components: {Header},
  data() {
    return {
      blog: {
        id: '',
        title: '',
        content: ''
      },
      ownBlog: false
    }
  },
  // 加载页面时候请求这个blog
  created() {
    const blogId = this.$route.params.blogId
    const _this = this
    if (blogId) {
      this.$axios.get('/blog/' + blogId).then(res => {
        const blog = res.data.data
        _this.blog.id = blog.blogId
        _this.blog.title = blog.title

        // 把markdown格式进行渲染,渲染后是html格式
        var MarkdownIt = require("markdown-it")
        var md = new MarkdownIt()
        _this.blog.content = md.render(blog.content)

        // 当博客的userId等于当前登录id,编辑按钮才展示
        _this.ownBlog = (blog.userId === this.$store.getters.getUserInfo.id)

      })
    }
  }
}
</script>

<style scoped>
.mblog {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 100%;
  min-height: 700px;
  padding: 20px 15px;
}
</style>