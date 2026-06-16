import Character from "./Character";

class Enemy extends Character {
    constructor(
        x,
        y,
        width,
        height,
        speed = 2,
        canvasWidth = 1350,
        damage = 1,
        hp = 1,
        color = "blue",
        canJump = false
    ) {
        super(x, y, width, height);
        this.velocityX = speed;
        this.canvasWidth = canvasWidth;
        this.damage = damage;
        this.hp = hp;
        this.color = color;
        this.canJump = canJump;
    }

    draw(ctx) {
        ctx.fillStyle = this.color;
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }

    move() {
        this.x += this.velocityX;

        if (this.x + this.width >= this.canvasWidth || this.x <= 0) {
            this.velocityX *= -1;
        }
    }

    checkCollision(player) {
        return (
            player.x + player.width > this.x &&
            player.x < this.x + this.width &&
            player.y + player.height > this.y &&
            player.y < this.y + this.height
        );
    }
}

export default Enemy;