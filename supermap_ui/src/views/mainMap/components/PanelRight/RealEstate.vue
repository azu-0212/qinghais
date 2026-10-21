<template>
  <div class="realEstatePanel">
    <!-- <el-input
      class="treeSearch"
      prefix-icon="el-icon-search"
      v-model="treeFilterText"
      placeholder="输入您要查询的内容"
    /> -->
    <div class="table-container">
      <table class="table">
        <tr class="header">
          <th v-for="(item, index) in itemsList" :key="index">{{ item }}</th>
        </tr>
        <tr
          :class="['data-item', { active: buildIndex === index }]"
          v-for="(item, index) in buildingsList"
          :key="index"
          @click="chooseBuild(item)"
        >
          <td style="position: relative">
            {{ index + 1 }}
            <span
              class="icon-box"
              v-if="index == 4"
              @click.stop="openOrclose"
              title="住户信息"
            >
              <i v-show="isexpan" class="el-icon-arrow-up"></i>
              <i v-show="!isexpan" class="el-icon-arrow-down"></i>
            </span>
          </td>
          <td>{{ item.data.CONTRACTOR }}</td>
          <td>{{ item.data.PLACENAME }}</td>
          <td>{{ parseInt(item.data.HOUSEAREA) }}</td>
          <td>{{ item.data.LANDUSE }}</td>
          <td>{{ item.data.TYPE }}</td>
          <td>{{ item.data.GEOLEVEL }}</td>
        </tr>
      </table>

      <!-- 子表格：分层分户数据 -->
      <div v-if="isexpan" class="house-container">
        <table class="house-table">
          <tr class="header">
            <th v-for="(item, index) in houseTitle" :key="index">
              {{ item }}
            </th>
          </tr>
          <tr
            :class="['data-item', { active: houseIndex === index }]"
            v-for="(item, index) in householdsList"
            :key="index"
            @click="chooseHouse(item)"
          >
            <td style="position: relative">
              {{ index + 1 }}
            </td>
            <td>{{ item.data.HOUSEHOLDER }}</td>
            <td>{{ parseInt(item.data.FAMILY) }}</td>
            <td>{{ parseInt(item.data.HOUSENUMBER) }}</td>
            <td>{{ parseInt(item.data.HOUSINGAREA) }}</td>
            <td>{{ getPhone(item.data.TELEPHONE) }}</td>
          </tr>
        </table>
      </div>
      <!-- 气泡 -->
      <!-- <div id="popupContainer"></div> -->
    </div>
  </div>
</template>

<script>
import dataVue from "../../../system/dict/data.vue";
import queryServer from "@/utils/supermap/map3D/queryServer";
export default {
  name: "RealEstate",
  data() {
    return {
      treeFilterText: "",
      itemsList: [
        "序号",
        "承包方姓名",
        "小地名",
        "地块面积",
        "土地用途",
        "地类",
        "地理等级",
      ],
      houseTitle: [
        "序号",
        "户主姓名",
        "家庭人数",
        "门牌号",
        "房屋面积",
        "电话号码",
      ],
      buildingsList: [],
      householdsList: [],
      isexpan: false,
    };
  },
  computed: {
    buildIndex: function () {
      return this.$store.getters.buildIndex;
    },
    houseIndex: function () {
      return this.$store.getters.houseIndex;
    },
  },
  mounted() {
    this.loadData();
    queryServer.initBuildHandler();
  },
  methods: {
    loadData() {
      let _this = this;
      queryServer.queryAreaInstanceBuildings().then(function (res) {
        _this.buildingsList = res.result.features;
      });
      queryServer.queryAreaInstanceHouseholders().then(function (res) {
        _this.householdsList = res.result.features;
        console.log("分层分户数据", res);
      });
    },
    chooseBuild(item) {
      let center = {
        lng:
          (parseFloat(item.data.SMSDRIE) + parseFloat(item.data.SMSDRIW)) / 2,
        lat:
          (parseFloat(item.data.SMSDRIN) + parseFloat(item.data.SMSDRIS)) / 2,
      };
      // 查询点对象
      let queryPoint = {
        x: parseFloat(item.data.LNG),
        y: parseFloat(item.data.LAT),
      };
      queryServer.queryByPoint(queryPoint, "list", center);
    },

    chooseHouse(item) {
      let params = {
        lng: parseFloat(item.data.LNG),
        lat: parseFloat(item.data.LAT),
        height: parseFloat(item.data.HEIGHT),
      };
      queryServer.queryHouse(params);
    },

    openOrclose() {
      this.isexpan = !this.isexpan;
    },
    getPhone(str) {
      if (str) {
        return str.split(".").join("").slice(0, 11);
      } else {
        return "";
      }
    },
  },
};
</script>

<style scoped>
.realEstatePanel {
  width: 100%;
  height: calc(100% - 55px);
}
.treeSearch {
  width: 90%;
  margin: 10px 15px;
}
.treeSearch >>> .el-input__inner {
  background: transparent;
  color: #16dfe2;
}
.table-container {
  width: 100%;
  color: #fff;
  white-space: nowrap;
  text-align: center;
  font-size: 15px;
  margin-top: 15px;
  overflow: auto;
}
.table-container .table {
  border-collapse: collapse;
}
/* .table .active {
  background-color: rgba(24, 247, 248, 0.4);
} */
.active {
  background-color: rgba(24, 247, 248, 0.4);
}
th {
  color: #18f7f8;
  padding: 8px;
}
td {
  padding: 12px;
}

/* 修改 滚动条 下面 的 宽度 */
.table-container::-webkit-scrollbar {
  height: 5px;
}
/* 修改 滚动条的 下面 的 样式 */
.table-container::-webkit-scrollbar-track {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 1em;
}
/* 修改 滑块 */
.table-container::-webkit-scrollbar-thumb {
  background-color: rgba(24, 247, 248, 0.7);
  border-radius: 1em;
}
.data-item:hover {
  background-color: rgba(24, 247, 248, 0.2);
}
.icon-box {
  position: absolute;
  right: 0px;
  /* cursor: crosshair; */
}

.house-container .house-table {
  border-collapse: collapse;
  font-size: 14px;
}
</style>
