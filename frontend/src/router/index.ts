import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/stream',
      name: 'stream',
      // route level code-splitting
      // this generates a separate chunk (StreamView.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/StreamView.vue')
    }
  ]
})

export default router