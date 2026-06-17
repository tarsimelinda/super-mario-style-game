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

        this.baseY = y;
        this.jumpPhase = Math.random() * Math.PI * 2;
    }

    draw(ctx) {
        ctx.fillStyle = this.color || "blue";
        ctx.fillRect(this.x, this.y, this.width, this.height);

        if (this.hp > 1) {
            ctx.fillStyle = "white";
            ctx.font = "14px Arial";
            ctx.fillText(this.hp, this.x + this.width / 2 - 4, this.y - 6);
        }
    }

    move() {
        this.x += this.velocityX;

        if (this.x + this.width >= this.canvasWidth || this.x <= 0) {
            this.velocityX *= -1;
        }

        if (this.canJump) {
            this.jumpPhase += 0.08;
            this.y = this.baseY + Math.sin(this.jumpPhase) * 20;
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

    isStompedBy(player) {
        const playerBottom = player.y + player.height;
        const previousPlayerBottom = playerBottom - player.velocityY;
        const enemyTop = this.y;

        const isFalling = player.velocityY > 0;
        const wasAboveEnemy = previousPlayerBottom <= enemyTop + 25;
        const isNowTouchingEnemy = playerBottom >= enemyTop;

        const horizontallyOverlapping =
            player.x + player.width > this.x + 5 &&
            player.x < this.x + this.width - 5;

        return (
            isFalling &&
            wasAboveEnemy &&
            isNowTouchingEnemy &&
            horizontallyOverlapping &&
            this.checkCollision(player)
        );
    }

    takeDamage(amount = 1) {
        this.hp = Math.max(0, this.hp - amount);
        return this.hp;
    }
}

export default Enemy;