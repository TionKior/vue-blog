import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        // userInfo和token初始化的时候可以用sessionStorage中去获取,记得反序列化
        token: localStorage.getItem("token"),
        userInfo: JSON.parse(sessionStorage.getItem("userInfo"))
    },
    mutations: {
        // set方法
        SET_TOKEN: (state, token) => {
            state.token = token
            // 放到LocalStore中,浏览器关闭还可以存到浏览器的LocalStore中
            localStorage.setItem("token", token);
        },
        SET_USERINFO: (state, userInfo) => {
            state.userInfo = userInfo
            // 放到sessionStorage保存到会话中,下一次也可以通过localStorage去获取userInfo信息
            // sessionStorage不能存储一个对象,需要进行序列化
            sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
        },
        // 删除操作
        REMOVE_INFO: (state) => {
            state.token = ''
            state.userInfo = {}
            localStorage.setItem("token", '');
            sessionStorage.setItem("userInfo", JSON.stringify(''));
        }

    },
    getters: {
        // get方法
        getUserInfo: state => {
            return state.userInfo
        }

    },
    actions: {},
    modules: {}
})
