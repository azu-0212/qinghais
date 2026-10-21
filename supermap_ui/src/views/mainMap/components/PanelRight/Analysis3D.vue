<template>
  <div class="container">
    <!-- 分析工具容器 -->
    <div class="analysis-container">
      <div
        class="analysis-item"
        v-for="(item, index) in analysisArr"
        :key="index"
        @click="changeItem(item, index)"
      >
        <img
          :src="selectIndex == index ? item.img_active : item.img"
          alt=""
          class="analysis-img"
        />
        <p class="analysis-name" :class="{ active: selectIndex == index }">
          {{ item.name }}
        </p>
      </div>
    </div>
    <hr class="line" />
    <!-- 通视分析 -->
    <div class="tool-bar" v-if="selectIndex == 0">
      <button type="button" class="button common" @click="chooseView">
        选取观察位置
      </button>
      <button type="button" class="button common" @click="addTarget">
        添加目标点
      </button>
      <button type="button" class="button common" @click="clear('通视')">
        清除
      </button>
    </div>
    <!-- 可视域分析 -->
    <div class="tool-bar" v-if="selectIndex == 1">
      <button type="button" class="button common" @click="drawView">
        绘制可视域
      </button>
      <button type="button" class="button common slopeCloseBtn" @click="clear('可视域')">
        清除
      </button>
    </div>
    <!-- BOX裁剪 -->
    <div class="tool-bar" v-if="selectIndex == 2">
      <button type="button" class="button common" @click="activeBoxClip">
        开始裁剪
      </button>
      <select class="select-box common" v-model="clipMode" @change="changeMode">
        <option value="clip_behind_all_plane">裁剪包围盒内</option>
        <option value="clip_behind_any_plane">裁剪包围盒外</option>
      </select>
      <button type="button" class="button common" @click="clear('BOX裁剪')">
        清除
      </button>
    </div>
    <!-- 坡度坡向分析 -->
    <div class="tool-bar" v-if="selectIndex == 3">
      <div class="setting-box">
        <div class="setting-item">
          <label>计算模式：</label>
          <select
            v-model="slopeOptions.computeMode"
            @change="changeOption()"
            class="select-box common"
          >
            <option value="ARM_REGION">指定多边形区域</option>
            <option value="ARM_ALL">全部区域参与分析</option>
            <option value="ARM_NONE">全部区域不参与分析</option>
          </select>
        </div>
        <div class="setting-item">
          <label>坡度区间：</label>
          <p style="margin: 5px 0">
            <label> 起：</label>
            <input
              type="range"
              min="0"
              max="90"
              v-model="slopeOptions.slope.start"
              @change="changeOption()"
            />
            <input
              type="number"
              min="0"
              max="90"
              v-model="slopeOptions.slope.start"
              @change="changeOption()"
            />
          </p>
          <p style="margin: 5px 0">
            <label> 终：</label>
            <input
              type="range"
              min="0"
              max="90"
              v-model="slopeOptions.slope.end"
              @change="changeOption()"
            />
            <input
              type="number"
              min="0"
              max="90"
              v-model="slopeOptions.slope.end"
              @change="changeOption()"
            />
          </p>
        </div>
        <div class="setting-item">
          <label>显示样式：</label>
          <br />
          <div style="display: flex; flex-direction: column">
            <label
              ><input
                type="radio"
                name="fill"
                value="FACE"
                v-model="slopeOptions.fillMode"
                @change="changeOption()"
              />显示填充颜色</label
            >
            <label
              ><input
                type="radio"
                name="fill"
                value="ARROW"
                v-model="slopeOptions.fillMode"
                @change="changeOption()"
              />显示坡向箭头</label
            >
            <label
              ><input
                type="radio"
                name="fill"
                value="FACE_AND_ARROW"
                v-model="slopeOptions.fillMode"
                @change="changeOption()"
              />填充颜色和坡向箭头</label
            >
          </div>
        </div>
        <div class="setting-item">
          <label>颜色条带：</label>
          <div class="pickColor" @click="isShowColor = !isShowColor">
            <span
              class="currectColor"
              :style="{
                background: 'linear-gradient(to right,' + currectBackg + ' )',
              }"
            ></span>
            <i
              class="el-icon-arrow-down icon"
              v-show="!isShowColor"
              @click="isShowColor = true"
            ></i
            ><i
              class="el-icon-arrow-up icon"
              v-show="isShowColor"
              @click="isShowColor = false"
            ></i>
            <div v-show="isShowColor" class="option-container">
              <span
                class="color-item"
                v-for="(item, index) in analysis3DUtil.ribbon"
                :key="index"
                :style="{
                  background:
                    'linear-gradient(to right,' + getBackg(item.colors) + ' )',
                }"
                @click="changeColor(item.colors)"
              ></span>
            </div>
          </div>
        </div>
        <div class="setting-item">
          <label>透明调节：</label>
          <input
            type="number"
            step="0.1"
            min="0"
            max="1"
            v-model="slopeOptions.opacity"
            @change="changeOption()"
          />
        </div>
      </div>
      <button type="button" class="button common" @click="beginDraw">
        开始绘制分析区域
      </button>
      <button type="button" class="button common slopeCloseBtn" @click="clear('坡度坡向')">
        清除
      </button>
    </div>
  </div>
</template>

<script>
import MapUtil from "@/utils/supermap";
export default {
  name: "Analysis3D",
  data() {
    return {
      analysis3DUtil: null,
      selectIndex: 0,
      analysisArr: [
        {
          name: "通视分析",
          img: require("@/assets/image/analysis/sightline.png"),
          img_active: require("@/assets/image/analysis/sightline_active.png"),
        },
        {
          name: "可视域分析",
          img: require("@/assets/image/analysis/viewshed.png"),
          img_active: require("@/assets/image/analysis/viewshed_active.png"),
        },
        {
          name: "BOX裁剪",
          img: require("@/assets/image/analysis/boxCrop.png"),
          img_active: require("@/assets/image/analysis/boxCrop_active.png"),
        },
        {
          name: "坡度坡向分析",
          img: require("@/assets/image/analysis/slope.png"),
          img_active: require("@/assets/image/analysis/slope_active.png"),
        },
      ],
      clipMode: "clip_behind_all_plane", // 裁剪模式
      // 坡度坡向分析选项
      slopeOptions: {
        computeMode: "ARM_REGION", // 计算模式
        slope: {
          start: 0, // 起始坡度
          end: 80, // 终止坡度
        },
        fillMode: "FACE_AND_ARROW", // 填充模式
        colorTable: [], //色带设置
        opacity: 0.5,
      },
      isShowColor: false,
      currectBackg:
        "rgb(9, 9, 255),rgb(0, 161, 1),rgb(20, 187, 18),rgb(221, 224, 7),rgb(255, 0, 0)",
    };
  },
  mounted() {
    this.analysis3DUtil = MapUtil.SceneUtil.analysis3DUtil;
  },

  beforeDestroy() {
    this.analysis3DUtil.removeAll();
  },
  methods: {
    changeItem(item, index) {
      this.selectIndex = index;
      if (index == 3) {
        this.analysis3DUtil.slopeAnalysis();
      }
      // this.analysis3DUtil.removeAll();
    },
    // 通视分析选择观察点
    chooseView() {
      this.analysis3DUtil.chooseView();
    },
    // 通视分析选择目标点
    addTarget() {
      this.analysis3DUtil.addTarget();
    },
    // 可视域分析绘制可视域
    drawView() {
      this.analysis3DUtil.chooseViewPoint();
    },
    // 清除
    clear(type) {
      this.analysis3DUtil.clear(type);
    },
    // 激活BOX裁剪功能
    activeBoxClip() {
      this.analysis3DUtil.activeBoxClip(this.clipMode);
    },
    // BOX裁剪切换模式
    changeMode() {
      this.analysis3DUtil.changeClipMode(this.clipMode);
    },
    // 坡度坡向分析激活绘制分析面
    beginDraw() {
      this.analysis3DUtil.beginDraw();
    },
    // 度坡向分析修改参数
    changeOption() {
      this.analysis3DUtil.changeSlope(this.slopeOptions);
    },
    // 获取当前选中色带
    getBackg(colors) {
      let arr = [];
      colors.forEach((val) => {
        arr.push("rgb(" + val.color + ")");
      });
      return arr.join(",");
    },
    // 选取颜色
    changeColor(colors) {
      this.currectBackg = this.getBackg(colors);
      this.isShowColor = false;
      // 构造色带数据
      let arr = [];
      colors.forEach((item) => {
        let a1 = [];
        item.color.split(",").forEach((val) => {
          a1.push(parseInt(val) / 255);
        });
        let obj = {
          slope: item.slope,
          color: a1,
        };
        arr.push(obj);
      });
      this.slopeOptions.colorTable = arr;
      this.changeOption(this.slopeOptions);
    },
  },
};
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  color: #fff;
  .analysis-container {
    width: 82%;
    margin: 10px auto;
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    .analysis-item {
      width: 48%;
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-bottom: 10px;
      img {
        width: 42%;
      }
      p {
        font-size: 14px;
        margin: 10px 0;
      }
      .active {
        font-weight: 600;
        color: #0ffcf8;
      }
    }
  }
  .line {
    margin-bottom: 20px;
    padding: 0;
    background-image: linear-gradient(
      to right,
      transparent,
      rgba(24, 247, 248, 0.26),
      transparent
    );
    border: 0;
    height: 3px;
  }
  .tool-bar {
    width: 80%;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    align-items: center;
    .common {
      width: 60%;
      line-height: 40px;
      background-color: rgba(1, 1, 20, 0.5);
      margin-top: 15px;
      color: #fff;
      box-shadow: 0 0 5px 3px rgba(25, 235, 235, 0.2);
      cursor: pointer;
      border: none;
      outline: none;
      border-radius: 8px;
    }
    .select-box {
      height: 40px;
      padding: 5px;
      option {
        // background-color: rgba(1, 1, 20, 0.5);
        // height: 60px;
        margin: 10px;
        background-color: rgba(0, 0, 0, 0.8);
      }
    }
    .button {
      &:hover {
        background-color: rgba(25, 235, 235, 0.4);
      }
    }
    .setting-box .setting-item {
      margin-bottom: 10px;
      /* 色带样式 */
      .pickColor {
        width: 85%;
        position: relative;
        height: 36px;
        line-height: 36px;
        background-color: #fff;
        color: #404040;
        font-size: 20px;
        border-radius: 8px;
        .currectColor {
          width: 80%;
          height: 24px;
          position: absolute;
          top: 6px;
          left: 8px;
          border-radius: 3px;
        }
        .icon {
          position: absolute;
          top: 8px;
          right: 10px;
        }
        .option-container {
          position: absolute;
          height: 120px;
          width: 100%;
          top: 36px;
          background-color: #fff;
          overflow: scroll;
          &::-webkit-scrollbar {
            width: 0;
          }
          .color-item {
            display: block;
            width: 88%;
            height: 20px;
            text-align: center;
            margin: 15px auto;
            // border: 2px solid rgb(25, 235, 235);
          }
        }
      }
    }
  }
}
</style>
