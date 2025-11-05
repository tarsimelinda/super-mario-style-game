export default class Physics {
  constructor(gravity = 0.5, jumpPower = 15) {
    this.gravity = gravity;
    this.jumpPower = jumpPower;
  }

  applyGravity(player, groundY) {
    player.velocityY += this.gravity;
    player.y += player.velocityY;

    if (player.y + player.height >= groundY) {
      player.y = groundY - player.height;
      player.velocityY = 0;
      player.canJump = true;
    }
  }

  jump(player) {
    if (player.canJump) {
      player.velocityY = -this.jumpPower;
      player.canJump = false;
    }
  }
}
