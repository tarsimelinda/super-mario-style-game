export default class Physics {
    constructor(gravity = 0.5, jumpPower = 15) {
      this.gravity = gravity;
      this.jumpPower = jumpPower;
    }
  
    applyGravity(player, groundY) {
        if (player.y + player.height < groundY || player.velocityY < 0) {
          player.velocityY += this.gravity;
          player.y += player.velocityY;
        } else {
          if (player.velocityY > 0) {
            player.y = groundY - player.height;
            player.velocityY = 0;
            player.canJump = true;
          }
        }
        //console.log(player.velocityY)

      //  console.log(player.y, player.velocityY);
      }
      
  
      jump(player) {
        if (player.canJump) {
          player.velocityY = -this.jumpPower;
         // console.log(player.velocityY);
          player.canJump = false;
        }
        //console.log(player.velocityY)

      }
  }
  