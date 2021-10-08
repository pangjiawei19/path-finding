async function findPath(startPoint, endPoint) {
    const res = await axios.post('/map/findPath', {
        mapKey: mapKey,
        start: startPoint,
        end: endPoint,
        direction: direction,
        mergePath: mergePath
    });

    const pointList = res.data;
    if (!pointList) {
        return [];
    }
    return pointList;
}

function getColorByNodeType(nodeType) {
    let color;
    switch (nodeType) {
        case 0:
            color = '#ffecb3';
            break;
        case 2:
            color = '#ffb300';
            break;
        case 3:
            color = '#2396f3';
            break;
        case 4:
            color = '#00ff00';
            break;
        default:
            color = '#000000';
            break;
    }
    return color;
}

function getClickPoint(ev) {
    const pixel = {
        x: ev.layerX, // 0 ~ width-1
        y: ev.layerY  // 0 ~ height-1
    }
    return calculatePointByPixel(pixel);
}

function isSamePoint(point1, point2) {
    return point1.col === point2.col && point1.row === point2.row;
}

function isSamePixel(pixel1, pixel2) {
    return pixel1.x === pixel2.x && pixel1.y === pixel2.y;
}

function calculatePointByPixel(pixel) {
    const col = Math.floor(pixel.x / ratio); // 0 ~ width-1
    const row = Math.floor(pixel.y / ratio); // 0 ~ height-1
    return {col, row};
}

function calculatePixelByPoint(point) {
    const radius = ratio / 2;
    const x = point.col * ratio + radius;
    const y = point.row * ratio + radius;
    return {x, y};
}

function calculateMoveUnit(point1Pixel, point2Pixel, speed) {
    // 计算距离
    const diffColPixel = Math.abs(point2Pixel.x - point1Pixel.x);
    const diffRowPixel = Math.abs(point2Pixel.y - point1Pixel.y);
    // const distance = Math.ceil(Math.sqrt(diffColPixel * diffColPixel + diffRowPixel * diffRowPixel));
    const distance = Math.sqrt(diffColPixel * diffColPixel + diffRowPixel * diffRowPixel);

    // 计算移动单位距离
    const moveColPixel = speed * diffColPixel / distance;
    const moveRowPixel = speed * diffRowPixel / distance;

    return {colPixel: moveColPixel, rowPixel: moveRowPixel};
}

function calculateNextPixel(sourcePixel, targetPixel, moveUnit) {
    let newX = sourcePixel.x;
    let newY = sourcePixel.y;
    const forwardX = targetPixel.x > newX ? 1 : -1;
    const forwardY = targetPixel.y > newY ? 1 : -1;
    if (newX !== targetPixel.x) {
        newX += moveUnit.colPixel * forwardX;
        if ((targetPixel.x - sourcePixel.x) * (targetPixel.x - newX) < 1) {
            newX = targetPixel.x;
        }
    }
    if (newY !== targetPixel.y) {
        newY += moveUnit.rowPixel * forwardY;
        if ((targetPixel.y - sourcePixel.y) * (targetPixel.y - newY) < 1) {
            newY = targetPixel.y;
        }
    }
    return {x: newX, y: newY};
}

function randomPointCanMoveByColAndRow(minCol, maxCol, minRow, maxRow, mapArray) {
    const pointList = [];
    for (let i = minCol; i <= maxCol; i++) {
        for (let j = minRow; j <= maxRow; j++) {
            const point = {col: i, row: j};
            if (isPointCanMove(point, mapArray)) {
                pointList.push(point);
            }
        }
    }
    if (!pointList.length) {
        return null;
    }
    return pointList[Math.floor(Math.random() * pointList.length)];
}

function randomSurroundingPointCanMove(point, mapArray, actorList) {
    let pointList = getSurroundingPoint(point, mapArray, 1);
    if (!pointList.length) {
        return null;
    }
    pointList = pointList.filter(point => {
        return isPointCanMove(point, mapArray, actorList);
    });
    if (!pointList.length) {
        return null;
    }
    return pointList[Math.floor(Math.random() * pointList.length)];
}

function getSurroundingPoint(point, mapArray, layer) {
    const surroundingPointList = [];
    layer = layer || 1;
    for (let colDiff = -layer; colDiff <= layer; colDiff++) {
        for (let rowDiff = -layer; rowDiff <= layer; rowDiff++) {
            if (colDiff === 0 && rowDiff === 0) {
                continue;
            }
            let newCol = point.col + colDiff;
            let newRow = point.row + rowDiff;
            if (isValidPointByColAndRow(newCol, newRow, mapArray)) {
                surroundingPointList.push({col: newCol, row: newRow});
            }
        }
    }
    return surroundingPointList;
}

function isValidPointByColAndRow(col, row, mapArray) {
    const mapHeight = mapArray.length;
    const mapWidth = mapArray[0].length;
    return col >= 0
        && col < mapWidth
        && row >= 0
        && row < mapHeight;
}

function isPointCanMove(point, mapArray, actorList) {
    if (mapArray[point.row][point.col] !== 0) {
        return false;
    }
    if (!actorList) {
        return true;
    }
    return !actorList.some(actor => {
        const userPoint = actor.userPoint;
        return userPoint.col === point.col && userPoint.row === point.row;
    })
}