<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="节点名" prop="nodeName">
        <el-input
          v-model="queryParams.nodeName"
          placeholder="请输入节点名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据类型" prop="dataType">
        <el-select v-model="queryParams.dataType" placeholder="请选择数据类型" clearable size="small">
          <el-option v-for="(item,i) in layerType" :key="i"  :label="item.val" :value="item.key"/>
        </el-select>
      </el-form-item>
      <el-form-item label="图层名称" prop="childNames">
        <el-input
          v-model="queryParams.childNames"
          placeholder="请输入图层名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:resource:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:resource:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAddAuthority"
          v-hasPermi="['system:resource:add']"
        >分配权限</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading"
              row-key="resourceId"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
              :data="resourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="节点名" align="left" prop="nodeName"/>
      <el-table-column label="数据类型" align="center" prop="dataType">
        <template slot-scope="scope">
          <span v-if="scope.row.dataType !== -1">{{ layerType[scope.row.dataType].val }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务地址" align="center" prop="serverUrl"/>
      <el-table-column label="数据服务地址" align="center" prop="dataServerUrl"/>
      <el-table-column label="年份(年)" align="center" prop="dataYear"/>
      <el-table-column label="体量(GB)" align="center" prop="dataVolume"/>
      <el-table-column label="所属区域" align="center" prop="dataRegion"/>
      <el-table-column label="高程值" align="center" prop="height"/>
      <el-table-column label="排序" align="center" prop="orderBy"/>
      <el-table-column label="节点状态" align="center" prop="nodeStatus">
        <template slot-scope="scope">
          <div @click="getRow(scope.row)">
            <el-switch
              v-model="scope.row.nodeStatus"
              active-color="green"
              inactive-color="red"
              :active-value="2"
              :inactive-value="1"
              @change="changeSwitch"
            > </el-switch>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.nodeType === 0||scope.row.nodeType === 2"
            type="text"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd(scope.row)"
            style="color: green"
            v-hasPermi="['system:resource:add']"
          >新增
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:resource:edit']"
          >修改
          </el-button>
          <el-button
            v-if="!scope.row.children||scope.row.children.length===0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            style="color: red"
            v-hasPermi="['system:resource:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加按用户权限分配的 自然资源 数据结构   -->
    <el-dialog :close-on-click-modal="false" :title="authTitle" :visible.sync="authOpen" width="800px" append-to-body>
      <el-form ref="authForm" :model="authForm" :rules="authRules" label-width="80px">
        <el-form-item label="用户角色" prop="roleType">
          <el-select  v-model="authForm.roleType" @change="nodeTypeChanged" placeholder="请选择用户角色" style="width:100%" >
            <el-option v-for="(item,i) in roleType"  selected :key="i" :label="item.roleName" :value="item.roleKey"/>
          </el-select>
        </el-form-item>
        <el-form-item label="目录权限">
          <el-tree
            ref="auto"
            :data="resourceOptions"
            show-checkbox
            node-key="resourceId"
            empty-text="加载中，请稍后"
            :props="defaultProps"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="autoSubmitForm">确 定</el-button>
        <el-button @click="authCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改数据资源对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="节点名" prop="nodeName">
          <el-input v-model="form.nodeName" placeholder="请输入节点名"/>
        </el-form-item>
        <el-form-item label="节点类型">
          <el-select  v-model="form.nodeType" @change="nodeTypeChanged" placeholder="请选择节点类型">
            <el-option v-for="(item,i) in nodeType"  selected :key="i" :label="item.dictLabel" :value="item.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.nodeType && form.nodeType !== '0'" label="数据类型">
          <el-select  v-model="form.dataType" placeholder="请选择数据类型">
            <el-option v-for="(item,i) in layerType"  selected :key="i" :label="item.val" :value="item.key"/>
          </el-select>
        </el-form-item>

        <el-form-item v-if="form.nodeType && form.nodeType!== '0'" label="服务地址" prop="serverUrl">
          <el-input v-model="form.serverUrl" placeholder="请输入服务地址"/>
        </el-form-item>

        <!--请输入年份-->
        <el-form-item v-if="form.nodeType && form.nodeType!== '0'" label="数据年份" prop="dataYear">
          <el-input v-model="form.dataYear" placeholder="请输入数据年份"/>
        </el-form-item>

        <!--请输入数据体量-->
        <el-form-item v-if="form.nodeType && form.nodeType!== '0'" label="数据体量" prop="dataVolume">
          <el-input v-model="form.dataVolume" placeholder="请输入数据体量"/>
        </el-form-item>

        <!--请输入序号-->
        <el-form-item label="序号" prop="orderBy">
          <el-input v-model="form.orderBy" placeholder="请输入序号"/>
        </el-form-item>
        <!--        <el-form-item label="排序依据" prop="orderBy">-->
        <!--          <el-input v-model="form.orderBy" placeholder="请输入排序依据"/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="图例" prop="legend">-->
        <!--          <el-input v-model="form.legend" placeholder="请输入图例"/>-->
        <!--        </el-form-item>-->
        <el-form-item v-if="form.dataType === '2'" label="图层名称" prop="childNames">
          <el-input v-model="form.childNames" placeholder="请输入图层名称"/>
        </el-form-item>
        <!--        <el-form-item label="筛选图层" prop="layerSearch">-->
        <!--          <el-input v-model="form.layerSearch" placeholder="筛选图层"/>-->
        <!--        </el-form-item>-->
        <el-form-item v-if="form.dataType === '2'" label="查询参数" prop="queryPara">
          <el-input v-model="form.queryPara" placeholder="请输入查询参数"/>
        </el-form-item>

        <!--当数据为全景、街景、倾斜摄影时需要加position参数-->
        <el-form-item v-if="form.dataType === '1' || form.dataType === 1 || form.dataType === '3' || form.dataType === 3 || form.dataType === '5' || form.dataType === 5" label="position" prop="position">
          <el-input v-model="form.position" type="textarea" :rows="3" placeholder="请输入position"/>
        </el-form-item>

        <!--倾斜摄影,街景数据，全景数据选择所属区域-->
        <el-form-item v-if="form.dataType === '1' || form.dataType === 1 || form.dataType === '5' || form.dataType === 5 || form.dataType === '3' || form.dataType === 3 " label="所属区域" prop="dataRegion">
          <el-select  v-model="form.dataRegion" placeholder="请选择数据类型">
            <el-option v-for="(item,i) in regionType"  selected :key="i" :label="item.dictLabel" :value="item.dictValue"/>
          </el-select>
        </el-form-item>

        <!--倾斜摄影,街景数据，全景数据输入高程值-->
        <el-form-item v-if="form.dataType === '1' || form.dataType === 1 " label="高程值" prop="height">
          <el-input v-model="form.height" placeholder="请输入height高程值"/>
        </el-form-item>

        <!--当输入数据为WMTS服务时设置参数-->
        <el-form-item v-if="form.dataType === '4' || form.dataType === 4" label="dataParams" prop="dataParam">
          <el-input v-model="form.dataParam" type="textarea" :rows="6" placeholder="请输入dataParam,例如：{'layer':'世界地图_Day','style':'default','format':'image/png','tileMatrixSetID':'GoogleMapsCompatible_世界地图_Day','tileWidth':256,'tileHeight':256,'ids':21},注意以json格式输入"/>
        </el-form-item>

        <!--当输入数据为栅格瓦片数据时设置参数-->
        <el-form-item v-if="form.dataType === '8' || form.dataType === 8" label="数据源" prop="dataSource">
          <el-input v-model="form.dataSource" type="textarea" placeholder="请输入数据源"/>
        </el-form-item>

        <!--当输入数据为 栅格瓦片/图片库点位 数据时设置参数-->
        <el-form-item v-if="form.dataType === '8' || form.dataType === 8" label="数据集" prop="dataSet">
          <el-input v-model="form.dataSet" type="textarea" placeholder="请输入数据集"/>
        </el-form-item>
        <!--当输入数据为 栅格瓦片数据 时设置参数-->
        <el-form-item v-if="form.dataType === '8' || form.dataType === 8" label="数据服务地址" prop="dataServerUrl">
          <el-input v-model="form.dataServerUrl" placeholder="请输入数据服务地址"/>
        </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </template>

<script>
  import {
    listResource,
    getResource,
    delResource,
    addResource,
    updateResource,
    exportResource,
    updateResourceStatus,
    resourceAssignRoles,
    deleteAssignRoles
  } from "@/api/system/resource";
  import {listRole} from "@/api/system/role";
  export default {
    name: "Resource",
    data() {
      let inputParam = (rule, value,callback)=>{
        if(value === undefined || value === '' || value === null){
          callback(new Error('输入格式错误，请重新检查！请按json格式输入：{ }'));
        }else{
          try{
            JSON.parse(value);
          }catch (err){
            callback(new Error('输入格式错误，请重新检查！请按json格式输入：{ }'));
          }
          callback();
        }
      };
      let inputPosition = (rule, value,callback)=>{
        if(value === undefined || value === '' || value === null){
          callback(new Error('输入格式错误，请重新检查！请按json格式输入：{ }'));
        }else{
          try{
            JSON.parse(value);
          }catch (err){
            callback(new Error('输入格式错误，请重新检查！请按json格式输入：{ }'));
          }
          callback();
        }
      };
      return {
        layerType: [
          {val:"影像图层"},
          {val:"倾斜摄影"},
          {val:"MVT图层"},
          {val:"全景数据"},
          {val:"WMTS影像"},
          {val:"街景数据"},
          {val:"点云数据"},
          {val:"照片类型"}
        ],
        nodeType: [],
        roleType:[],
        // 数据所属类型
        regionType:[],
        resourceId:0,
        status:1,
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        // 数据资源表格数据
        resourceList: [],
        resourceAutoIds: [],
        // 弹出层标题
        title: "",
        // 权限分配弹出层标题
        authTitle: "",
        // 是否显示弹出层
        open: false,
        authOpen: false,
        layer : "世界地图_Day",
        style : "default",
        format : "image/png",
        tileMatrixSetID : "GoogleMapsCompatible_世界地图_Day",
        tileWidth : 256,
        tileHeight : 256,
        lon:0,
        lat:0,
        alt:0,
        // 查询参数
        queryParams: {
          parentId: undefined,
          nodeName: undefined,
          nodeType: undefined,
          dataType: undefined,
          serverUrl: undefined,
          orderBy: undefined,
          nodeStatus: undefined,
          legend: undefined,
          layerSearch: undefined,
          queryPara: undefined,
          childNames: undefined,
          createDate: undefined,
          updateDate: undefined,
          bindArea: undefined,
          height:undefined
        },
        // 自然资源总目录
        resourceOptions: [],
        defaultProps: {
          children: 'children',
          label: 'nodeName'
        },
        // 表单参数
        form: {},
        // 用户权限表单参数
        authForm: {},
        // 表单校验
        rules: {
          // WMTS params校验
          dataParam: [
            {required: true, validator:inputParam, trigger: "blur"}
          ],
          // position校验
          position: [
            {required: true, validator:inputPosition, trigger: "blur"}
          ],
         /* orderBy: [
            {required: true, message: '序号不能为空', trigger: "blur"}
          ],*/
          dataRegion:[
            {required: true, message: '所属区域不能为空', trigger: "blur"}
          ],
          dataSource:[
            {required: true, message: '数据源不能为空', trigger: "blur"}
          ],
          dataSet:[
            {required: true, message: '数据集不能为空', trigger: "blur"}
          ],
          dataServerUrl:[
            {required: true, message: '数据服务地址不能为空', trigger: "blur"}
          ]
        },
        // 用户权限表单校验
        authRules: {
          roleType:[
            {required: true, message: '用户角色不能为空', trigger: "blur"}
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getDicts('sys_layer_type').then(response => {
        this.layerType = [];
        response.data.map((item,i) => {
          this.layerType[i] = {};
          this.layerType[i].key = item.dictValue;
          this.layerType[i].val = item.dictLabel;
        });
      });
      this.getDicts('sys_region_type').then(response => {
        this.regionType = response.data;
      });
      this.getDicts('sys_node_type').then(response => {
        this.nodeType = response.data;
      });
      // 查询角色列表
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.roleType = response.rows.filter(item => {
            return item.roleId > 2
          })
        }
      )
    },
    methods: {
      /** 查询数据资源列表 */
      getList() {
        this.loading = true;
        listResource(this.queryParams).then(response => {
          this.resourceList = this.handleTree(response.rows, 'resourceId');
          this.resourceOptions = this.handleTreeChecked(response.rows, 'resourceId').filter((item)=>{
            return item.parentCode === 3;
          })[0].children;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 权限分配取消按钮
      authCancel(){
        this.authOpen = false;
        this.authReset();
      },
      // switch 开关
      changeSwitch(status){
        this.status = status;
      },
      // 获取resourceId
      getRow(val){
        this.resourceId = val.resourceId;
        this.updateStatus();
      },
      updateStatus(){
        let resourceId = this.resourceId;
        let status = this.status;
        updateResourceStatus(resourceId,status).then(res => {
          if (res.code === 200) {
            this.msgSuccess("数据状态修改成功");
            this.open = false;
          }else{
            this.msgError("数据状态修改失败");
            this.open = false;
          }
        })
      },
      // 表单重置
      reset() {
        this.form = {
          resourceId: undefined,
          parentId: undefined,
          nodeName: undefined,
          nodeType: undefined,
          dataType: undefined,
          dataRegion: undefined,
          serverUrl: undefined,
          dataServerUrl: undefined,
          orderBy: undefined,
          dataYear: undefined,
          dataVolume:undefined,
          nodeStatus: "0",
          legend: undefined,
          layerSearch: undefined,
          queryPara: undefined,
          childNames: undefined,
          createDate: undefined,
          updateDate: undefined,
          updateBy: undefined,
          bindArea: undefined,
          remark: undefined,
          dataSource:undefined,
          dataSet:undefined
        };
        this.resetForm("form");
      },
      // 权限分配表单重置
      authReset(){
        this.authForm = {
          roleType: undefined,
          resourceAutoIds: []
        }
        this.resetForm("authForm");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 所有目录节点数据
      getAutoAllCheckedKeys(){
        // 目前被选中的菜单节点
        const checkedKeys = this.$refs.auto.getHalfCheckedKeys()
        // 半选中的菜单节点
        const halfCheckedKeys = this.$refs.auto.getCheckedKeys()
        checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
        return checkedKeys
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.resourceId);
        this.single = selection.length !== 1;
        this.multiple = !selection.length;
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        if (row) {
          this.form.parentId = row.resourceId;
        } else {
          this.form.parentId = 0;
        }
        this.form.nodeStatus = 2;
        this.open = true;
        this.title = "添加数据资源";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const resourceId = row.resourceId || this.ids;
        getResource(resourceId).then(response => {
          this.form = response.data;
          this.form.parentType = row.nodeType;
          this.open = true;
          this.title = "修改数据资源";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.resourceId) {
              updateResource(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              addResource(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                }
              });
            }
          }
        });
      },
      /** 给不同用户分配不同 自然资源 树结构 */
      handleAddAuthority(){
        this.authOpen = true;
      },
      /* 资源目录分配提交 */
      autoSubmitForm(){
        this.$refs['authForm'].validate(valid => {
          if (valid) {
            this.authForm.resourceAutoIds = this.getAutoAllCheckedKeys();
            if(this.authForm.resourceAutoIds.length > 0){
              //清空历史分配
              deleteAssignRoles(this.authForm.roleType).then(response => {
                if(response.code === 200){
                   resourceAssignRoles(this.authForm).then(response => {
                     if (response.code === 200) {
                       this.msgSuccess("授权成功！");
                       this.authOpen = false;
                       this.getList();
                     }
                   })
                }
              })

            }else{
              this.msgError("目录还未授权哦！！！");
            }
          }
        })

      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const resourceIds = row.resourceId || this.ids;
        this.$confirm('是否确认删除数据资源编号为"' + resourceIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delResource(resourceIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出所有数据资源数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return exportResource(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function () {
        });
      },
      nodeTypeChanged() {
        if (this.form.nodeType === '0') {
          this.form.dataType = -1;
        } else {
          this.form.dataType = undefined;
        }
      }
    }
  };
</script>

