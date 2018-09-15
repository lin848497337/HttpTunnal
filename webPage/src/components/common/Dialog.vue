<template>
  <div class="modal" v-show="show" transition="fade">
  </div>
  <!--<div v-show="show" class="modal-backup" transition="fade"></div>-->
</template>


<script>
    export default {
        name: "dialog"
    }
</script>

<style scoped>
  .modal {
    position: fixed;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    z-index: 1001;
    -webkit-overflow-scrolling: touch;
    outline: 0;
    overflow: scroll;
    margin: 30/@rate auto;
  }
  .modal-dialog {
    position: absolute;
    left: 50%;
    top: 0;
    transform: translate(-50%,0);
    width: 690/@rate;
    padding: 50/@rate 40/@rate;
    background: #fff;
  }
  .modal-backup {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 1000;
    background: rgba(0, 0, 0, 0.5);
  }
</style>
