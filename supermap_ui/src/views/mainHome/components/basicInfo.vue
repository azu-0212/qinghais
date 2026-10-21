<template>
  <div class="basicMapper">
    <div class="visits">
      <p>{{ visits.title }}<span>{{ visits.numbers }}</span></p>
    </div>
    <div class="online">
      <p>{{ onlinePeople.title }}<span>{{ onlinePeople.numbers }}</span></p>
<!--      <p>{{ onlinePeople.title }}<span>{{ 1 }}</span></p>-->
    </div>
    <div class="current" style="color:#FFF">
      {{ currentTime }}
    </div>
    <div class="dateWeek">
      <span>{{ weekTime }}</span>
      <span>{{ dateTime }}</span>
    </div>
  </div>
</template>

<script>
  import { getNowFormatDate,getTimeSlot,conversionTime } from "@/utils/date";
  import { listLoginInfor} from "@/api/system/basic";
  import { list,getLoginOnline } from '@/api/monitor/online'
  export default {
    name: "basicInfo",
    data(){
      return {
        onlinePeople: {
          title: '在线人数',
          numbers: 0
        },
        visits: {
          title: '累计访问量',
          numbers: 10
        },
        currentTime: '10:30:00',
        weekTime: '星期五',
        dateTime: '2021/3/12'
      }
    },
    mounted(){
      let int = setInterval(this.getTime,1000);
    },
    created() {
      list().then(response => {
        this.onlinePeople.numbers = response.rows.length;
      });
      // console.log('获取时间',conversionTime(getNowFormatDate()));
      // console.log('获取时间段',conversionTime(getTimeSlot(0.5)));
     /* list().then(response => {
        let onlineArr = [];
        response.rows.map((item)=>{
          (onlineArr.indexOf(item.ipaddr) === -1) && onlineArr.push(item.ipaddr);
        })
        this.onlinePeople.numbers = onlineArr.length;
      })*/
      listLoginInfor().then(res=>{
        this.visits.numbers = res.rows.length;
      })
    },
    methods:{
      getTime(){
        let currentArr = getNowFormatDate().split(",");
        this.dateTime = currentArr[0];
        this.currentTime = currentArr[1];
        this.weekTime = currentArr[2];
      }
    }
  }
</script>

<style scoped>
  .basicMapper{
    position:absolute;
    top:15px;
    right:130px;
    color:#FFF;
  }
  p{
    margin:0;
  }
  .dateWeek ,.current, .visits, .online{
    position: absolute;
  }
  .current{
    right:110px;
  }
  .dateWeek{
    right:0;
  }
  .online{
    width:125px;
    right:345px;
  }
  .visits{
    top:0;
    right:235px;
    width:128px;

  }
  .online, .visits{
    font-style: italic;
    font-size:14px;
  }
  .dateWeek span{
    display: block;
    margin:0 25px 3px 15px;
    font-size:14px;
    color:#FFF;
  }
  .current, .visits span, .online span{
    font-size:28px;
    font-family:"黑体";
    color:#1cd9da;
    font-style: normal;
    margin-left:5px;
  }
</style>
