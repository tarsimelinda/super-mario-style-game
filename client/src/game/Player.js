import Character from "./Character";

class Player extends Character {

    constructor(x, y, width, height, speed, groundY = 550) {
        super(x, y, width, height);
        this.velocityY = 0;
        this.speed = speed;
        this.groundY = groundY;
        this.canJump = this.y + this.height >= groundY;
    }

    draw(ctx, color = "red") {
        ctx.fillStyle = color;
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }

    move(keys, canvasWidth) {
        if (!keys) return;

        if (keys.right) {
            this.x = Math.min(canvasWidth - this.width, this.x + this.speed);
        }
        if (keys.left) {
            this.x = Math.max(0, this.x - this.speed);
        }

        if (this.x < 0) this.x = 0;
        if (this.x + this.width > canvasWidth) this.x = canvasWidth - this.width;
    }

    reset(x, y) {
        this.x = x;
        this.y = y;
        this.velocityY = 0;
        this.canJump = this.y + this.height >= this.groundY;
    }
}

export default Player;
