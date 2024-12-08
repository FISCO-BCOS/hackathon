export function neonPath(canvas, config = {}) {
  const BW = config.blockWidth ?? 100
  const gap = config.gap ?? 80
  const rows = config.rows ?? 3
  const cols = config.cols ?? 4

  const w = canvas.width
  const h = canvas.height
  const ctx = canvas.getContext('2d')

  function Network() {
    this.crosses = []
    this.init()
  }

  Network.prototype.init = function() {
    for (let r = 0; r < rows; r++) {
      const row = []
      for (let c = 0; c < cols; c++) {
        row.push({
          x: BW / 2 + c * (BW + gap),
          y: BW / 2 + r * (BW + gap),
        })
      }
      this.crosses.push(row)
    }
    // console.log(this.crosses);
  }

  Network.prototype.randomPath = function() {
    const fc = parseInt(Math.random() * cols)
    const fr = parseInt(Math.random() * rows)

    let tc = parseInt(Math.random() * cols)
    while (tc === fc) {
      tc = parseInt(Math.random() * cols)
    }

    let tr = parseInt(Math.random() * rows)
    while (tr === fr) {
      tr = parseInt(Math.random() * rows)
    }

    const deltac = Math.abs(tc - fc)
    const deltar = Math.abs(tr - fr)

    let delr = 0
    let delc = 0

    if (deltar > deltac) {
      delr = (tr - fr) > 0 ? 1 : -1
    } else {
      delc = (tc - fc) > 0 ? 1 : -1
    }

    return {
      from: this.crosses[fr][fc],
      dis: BW + gap,
      dir: {
        x: delc,
        y: delr
      }
    }
  }

  const network = new Network()

  var opts = {
    count: rows * cols + 10,
    spawnChance: 0.3,
    sparkChance: 0.1,
    sparkDist: 10,
    sparkSize: 2,
    speed: 2.5,
    neonSize: 3,

    // 重绘时，上一次图像的透明度
    repaintAlpha: 0.9,
    // 主亮点阴影
    shadowToTimePropMult: 6,

    // color: 'hsl(hue, 100%, 0%)',
    // 色彩变化率
    // hueChange: .1
    color: 'hsl(6, 100%, 0%)'
  }

  // let tick = 0;
  const lines = []

  function Line() {
    this.reset()
  }

  Line.prototype.reset = function() {
    const { from, dis, dir } = network.randomPath()
    this.distance = dis

    this.x = from.x
    this.y = from.y
    // 速度
    this.vx = opts.speed * dir.x
    this.vy = opts.speed * dir.y
    // 进度为 0
    this.process = 0

    // this.color = opts.color.replace('hue', tick * opts.hueChange);
  }

  Line.prototype.step = function() {
    // 行程
    const rate = this.process / this.distance
    if (rate > 0.99) this.reset()

    // 这一步走的距离
    const sx = this.vx * (2 - rate)
    const sy = this.vy * (2 - rate)
    this.process += Math.sqrt(sx * sx + sy * sy)

    // 更新当前位置
    this.x += sx
    this.y += sy

    // 大亮点
    ctx.shadowBlur = rate * opts.shadowToTimePropMult
    ctx.fillStyle = ctx.shadowColor = this.color
    ctx.fillRect(this.x, this.y, opts.neonSize, opts.neonSize)

    // 周围的小火花点
    if (Math.random() < opts.sparkChance) {
      const xx = this.x + Math.random() * opts.sparkDist * (Math.random() < 0.5 ? 1 : -1) - opts.sparkSize / 2
      const yy = this.y + Math.random() * opts.sparkDist * (Math.random() < 0.5 ? 1 : -1) - opts.sparkSize / 2
      ctx.fillRect(xx, yy, opts.sparkSize, opts.sparkSize)
    }
  }

  function loop() {
    requestAnimationFrame(loop)
    // ++tick;

    // 下面这段代码值 $ 10M
    var data = ctx.getImageData(0, 0, w, h).data
    var newImgData = ctx.createImageData(w, h)
    for (var i = 0; i < newImgData.data.length; i += 4) {
      newImgData.data[i + 0] = data[i + 0]
      newImgData.data[i + 1] = data[i + 1]
      newImgData.data[i + 2] = data[i + 2]
      const alpha = data[i + 3] * opts.repaintAlpha
      // 设置一个 20 阈值，不会留痕迹
      newImgData.data[i + 3] = alpha > 20 ? alpha : 0
    }
    ctx.putImageData(newImgData, 0, 0)

    if (lines.length < opts.count && Math.random() < opts.spawnChance) { lines.push(new Line()) }

    lines.map(function(line) { line.step() })
  }

  return {
    init: function() {
      requestAnimationFrame(loop)
    }
  }
}
