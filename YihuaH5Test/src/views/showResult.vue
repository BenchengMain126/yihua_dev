<template>

  <div class="container">



  </div>

</template>

<script>
  import {
    XButton, Group, XTextarea
  } from 'vux'


  export default {
    components: {
      XButton, Group, XTextarea
    },
    data() {
      return {
        "result": '初始化数据'
      }
    },

    methods: {
      saveCache() {

      },


    }
  }
</script>


<style lang="less" scoped>


  .container {
    background-color: white;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    margin: 0;
    padding: 16px;
  }


</style>
