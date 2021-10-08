class User {
    uid;
    userPoint;
    userPixel;
    userColor;
    radius;
    targetPointIndex = -1;
    targetPointList = [];
    moveUnit;
    moveForDuplicate;

    constructor(uid, userPoint, userColor, radius, moveForDuplicate) {
        this.uid = uid;
        this.userPoint = userPoint;
        this.userPixel = calculatePixelByPoint(userPoint);
        this.userColor = userColor;
        this.radius = radius || ratio / 2;
        this.moveForDuplicate = moveForDuplicate || false;
        drawActor(this);
    }

    move() {
        if (!this.isMoving()) {
            this.stopMoving();
            this.checkDuplicate();
            return null;
        }

        if (this.targetPointIndex < 0) {
            // 新的移动列表
            const firstTargetPointPixel = calculatePixelByPoint(this.targetPointList[0]);
            this.moveUnit = calculateMoveUnit(this.userPixel, firstTargetPointPixel, speed);
            this.targetPointIndex = 0;
        }

        const targetPointPixel = calculatePixelByPoint(this.targetPointList[this.targetPointIndex]);

        let oldUserPoint = this.userPoint;
        this.userPixel = calculateNextPixel(this.userPixel, targetPointPixel, this.moveUnit);
        this.userPoint = calculatePointByPixel(this.userPixel);

        if (isSamePixel(this.userPixel, targetPointPixel)) {
            this.targetPointIndex++;
            if (this.targetPointIndex < this.targetPointList.length) {
                const nextTargetPointPixel = calculatePixelByPoint(this.targetPointList[this.targetPointIndex]);
                this.moveUnit = calculateMoveUnit(this.userPixel, nextTargetPointPixel, speed);
            }
        }

        return {oldPoint: oldUserPoint, userPixel: this.userPixel, userColor: this.userColor};
    }

    setTargetList(list) {
        this.stopMoving();
        this.targetPointList.push(...list);
    }

    followPoint(point, mapArray, actorList) {
        const randomPoint = randomSurroundingPointCanMove(point, mapArray, actorList);
        if (!randomPoint || isSamePoint(this.userPoint, randomPoint)) {
            return;
        }
        findPath(this.userPoint, randomPoint).then(pointList => {
            const targetList = pointList.slice(1).map(point => {
                return {col: point.col, row: point.row};
            });
            this.setTargetList(targetList);
        });
    }

    isMoving() {
        return this.targetPointList.length > 0
            && this.targetPointIndex < this.targetPointList.length;
    }

    stopMoving() {
        this.targetPointList = [];
        this.targetPointIndex = -1;
    }

    checkDuplicate() {
        if (!this.moveForDuplicate) {
            return;
        }
        let count = 0;
        let move = false;
        for (const actor of actorList) { // TODO actorList
            if (isSamePoint(this.userPoint, actor.userPoint) && !actor.isMoving()) {
                count++;
                if (count > 1) {
                    move = true;
                    break;
                }
            }
        }
        if (move) {
            const randomPoint = randomSurroundingPointCanMove(this.userPoint, mapArray, actorList); // TODO mapArray
            if (!randomPoint || isSamePoint(this.userPoint, randomPoint)) {
                return;
            }
            this.setTargetList([randomPoint]);
        }
    }

    get userPoint() {
        return this.userPoint;
    }

    get userPixel() {
        return this.userPixel;
    }

    get uid() {
        return this.uid;
    }

    get userColor() {
        return this.userColor;
    }
}

class Monster extends User {

    patrolMinPoint;
    patrolMaxPoint;

    constructor(uid, userPoint, userColor, radius, moveForDuplicate, patrolMinPoint, patrolMaxPoint) {
        super(uid, userPoint, userColor, radius, moveForDuplicate);
        this.patrolMinPoint = patrolMinPoint;
        this.patrolMaxPoint = patrolMaxPoint;
    }

    randomPatrolPoint(mapArray) {
        return randomPointCanMoveByColAndRow(
            this.patrolMinPoint.col,
            this.patrolMaxPoint.col,
            this.patrolMinPoint.row,
            this.patrolMaxPoint.row,
            mapArray);
    }

    isPointInMonsterArea(point) {
        return point.col >= this.patrolMinPoint.col
            && point.col <= this.patrolMaxPoint.col
            && point.row >= this.patrolMinPoint.row
            && point.row <= this.patrolMaxPoint.row;
    }

    monsterAttack(point, mapArray, actorList) {
        if (this.isPointInMonsterArea(point)) {
            this.followPoint(point, mapArray, actorList);
        }
    }

    patrol(mapArray, user) {
        if (this.isMoving()) {
            return;
        }
        if (user && this.isPointInMonsterArea(user.userPoint)) {
            return;
        }

        const patrolPoint = this.randomPatrolPoint(mapArray);
        if (patrolPoint !== null) {
            this.setTargetList([patrolPoint]);
        }
    }
}