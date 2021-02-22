<template>
    <div id="app">
        <keep-alive>
            <router-view v-if="$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"></router-view>

    </div>
</template>

<script>
    export default {
        name: 'app'
    }
</script>

<style>

    #app {
        margin: 0;
        padding: 0;
        width: 100%;
        height: 100%;
        background: #f5f5f5;
    }

</style>
