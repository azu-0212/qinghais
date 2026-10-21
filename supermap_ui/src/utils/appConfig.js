export default {
  cluserPoint2015: require("@/static/data/diliguoqing_2015.json"),
  // 2015 年份分段
  cluserPoint2015_1: require("@/static/data/diliguoqing2015/T2015_1.json"),
  cluserPoint2015_2: require("@/static/data/diliguoqing2015/T2015_2.json"),
  cluserPoint2015_3: require("@/static/data/diliguoqing2015/T2015_3.json"),
  cluserPoint2015_4: require("@/static/data/diliguoqing2015/T2015_4.json"),
  cluserPoint2015_5: require("@/static/data/diliguoqing2015/T2015_5.json"),
  cluserPoint2015_6: require("@/static/data/diliguoqing2015/T2015_6.json"),
  cluserPoint2015_7: require("@/static/data/diliguoqing2015/T2015_7.json"),
  cluserPoint2015_8: require("@/static/data/diliguoqing2015/T2015_8.json"),
  cluserPoint2015_9: require("@/static/data/diliguoqing2015/T2015_9.json"),
  cluserPoint2015_10: require("@/static/data/diliguoqing2015/T2015_10.json"),

  cluserPoint2017: require("@/static/data/diliguoqing_2017.json"),    //cluserPoint2017: require("@/static/data/diliguoqing_2017.json"),
  cluserPoint2018: require("@/static/data/diliguoqing_2018.json"),
  cluserPoint2019: require("@/static/data/diliguoqing_2019.json"),
  cluserPoint2020: require("@/static/data/diliguoqing_2020.json"),
  // 2020 年份分段
  cluserPoint2020_1: require("@/static/data/diliguoqing2020/T2020_1.json"),
  cluserPoint2020_2: require("@/static/data/diliguoqing2020/T2020_2.json"),
  cluserPoint2020_3: require("@/static/data/diliguoqing2020/T2020_3.json"),

  /* 矿业核查 */
  // 采矿权
  cluserPointMining: require("@/static/data/mining.json"),
  //cluserPointMiningTest: require("@/static/data/mining_test.json"),
  // 工业企业
  cluserPointIndustrialEnterprises: require("@/static/data/industrialEnterprises.json"),
  // 扰动生态
  cluserPointDisturbedEcology: require("@/static/data/disturbedEcology.json"),
  // 探矿权
  cluserPointProspecting: require("@/static/data/prospecting.json"),
  // 渣山采坑
  cluserPointSlagMountain: require("@/static/data/slagMountain.json"),

  /* 耕地“非农化” */
  cluserPointNonfarm: require("@/static/data/feinong/2021/point.json"),

  /* 卫片执法 */
  // 2021
  //部推送图斑
  cluserPointPush1: require("@/static/data/weipian/2021/push/point_1.json"),
  cluserPointPush2: require("@/static/data/weipian/2021/push/point_2.json"),
  //省级补充图斑
  cluserPointProvincial: require("@/static/data/weipian/2021/provincial/point.json"),
  //cluserPointProvincial: require("@/static/data/weipian/2021/provincial/weipianTest.json"),

  cesiumToken: process.env.VUE_APP_CESIUM_TOKEN || '',
  imageryProvider: "https://webst02.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}", //影像服务 使用高德*/
}
