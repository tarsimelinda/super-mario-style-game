import Character from "./Character";

class Player extends Character {
    constructor(x, y, width, height, speed, canJump = false) {
        super(x, y, width, height)
        this.velocityY = 0;
        this.speed = speed;
        this.canJump = canJump;
    }

    draw(ctx, color) {
        ctx.fillStyle = color;
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }

    move(keys, canvasWidth) {
        if (keys.right) this.x = Math.min(canvasWidth - this.width, this.x + this.speed);
        if (keys.left) this.x = Math.max(0, this.x - this.speed);
    }
}

export default Player;
