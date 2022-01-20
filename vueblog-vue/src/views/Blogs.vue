<template>
  <div>
    <Header></Header>

    <div class="block">
      <el-timeline>
        <!--timestamp需要绑定值-->
        <el-timeline-item v-for="blog in blogs" :timestamp="blog.created" placement="top">
          <el-card>
            <h4>
              <router-link :to="{name: 'BlogDetail', params:{blogId: blog.id}}">
                {{ blog.title }}
              </router-link>
            </h4>
            <p>{{ blog.description }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <el-pagination class="mpage"
                     background
                     layout="prev, pager, next"
                     :current-page="currentPage"
                     :page-size="pageSize"
                     :total="total"
                     @current-change=page>
      </el-pagination>

    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "Blogs",
  components: {Header},
  data() {
    return {
      // 这个就是请求到的数据,设置一些默认值,需要和后端返回的数据格式相同
      blogs: {},
      currentPage: 1,
      total: 0,
      pageSize: 5
    }
  },
  methods: {
    page(currentPage) {
      const _this = this
      _this.$axios.get("/blogs?currentPage=" + currentPage).then(res => {
        console.log(res)
        // 返回的数据中有一个data还有一个data,在data下面的records中才是它的数据
        let data = res.data.data
        _this.blogs = data.records
        _this.total = data.total
        _this.currentPage = data.current
        _this.pageSize = data.size

      })
    }
  },
  // 在页面加载完的时候自己调用请求的方法
  created() {
    this.page(1)
  }
}
</script>

<style scoped>
.mpage {
  margin: 0 auto;
  text-align: center;
}
</style>