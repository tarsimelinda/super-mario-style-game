import Character from "./Character";

class Coins extends Character {
    constructor(x, y, width, height) {
        super(x, y, width, height);
    }

    draw(ctx) {
        ctx.fillStyle = "gold";
        ctx.beginPath();
        ctx.arc(
            this.x + this.width / 2,
            this.y + this.height / 2,
            this.width / 2,
            0,
            Math.PI * 2
        );
        ctx.fill();
    }
}

export default Coins;
