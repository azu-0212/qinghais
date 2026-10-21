export default {
  postRenderListener() {
    this.scene.postRender.addEventListener(() => {
      // 遍历更新位置
      let newH = this.viewer.camera['positionCartographic']['height']
      // console.log(newH)
      if (this.oldH && newH) {
        let show = false
        if (newH > this.scp_show_limit && this.oldH <= this.scp_show_limit) {
          //摄像头爬升,
          this.S3MLayersNames.map(name => {
            let layer = this.scene.layers.find(name)
            if (layer) {
              layer.visible = false
            }
          })
        } else if (newH < this.scp_show_limit && this.oldH >= this.scp_show_limit) {
          //摄像头拉近,
          this.S3MLayersNames.map(name => {
            let layer = this.scene.layers.find(name)
            if (layer) {
              layer.visible = true
            }
          })
        }
        this.oldH = newH
      }
    })
  }
}

//
// console.log('binding Events!!!')
// let handlerPoint = new Cesium.DrawHandler(this.viewer, Cesium.DrawMode.Point)
// handlerPoint.activate()
// handlerPoint.drawEvt.addEventListener((res) => {
//   console.log('camera:',this.viewer.camera)
//   console.log('position:', this.viewer.camera.position)
//   console.log('roll:', this.viewer.camera.roll)
//   console.log('pitch:', this.viewer.camera.pitch)
//   console.log('heading:', this.viewer.camera.heading)
//   handlerPoint.activate()
// })
