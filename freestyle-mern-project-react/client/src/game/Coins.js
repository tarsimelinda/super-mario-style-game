import Character from "./Character";

class Coins extends Character {
    constructor(x, y, width, height) {
        super(x, y, width, height)
    }

    draw(ctx) {
        ctx.fillStyle = "yellow";
        ctx.fillRect(this.x, this.y, this.width, this.height);
    }

}

export default Coins;