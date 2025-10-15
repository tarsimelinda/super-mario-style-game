import Character from "./Character";

class Enemy extends Character{
    constructor(x, y, width, height) {
        super(x, y, width, height)
        this.velocityX = 2;
    }

    draw(ctx) {
        ctx.fillStyle = "blue";
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }

    move() {
        this.x += this.velocityX;
        if (this.x + this.width >= 1350 || this.x <= 0) {
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

