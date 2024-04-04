var pxBox, pyBox, pzBox, eyeXBox, eyeYBox, eyeZBox, spotXBox, spotYBox, spotZBox, directionXBox, directionYBox, directionZBox, upXBox, upYBox, upZBox, projectionRadio, orthographicSection, perspectiveSection, topBox, bottomBox, leftBox, rightBox, nearBoxParallel, farBoxParallel, fovBox, aspectRatioBox, nearBoxPerspective, farBoxPerspective, rxBox3d, ryBox3d, rzBox3d;

function setup() {
    pxBox = document.getElementById("px-box");
    pyBox = document.getElementById("py-box");
    pzBox = document.getElementById("pz-box");
    eyeXBox = document.getElementById("eye-x-box");
    eyeYBox = document.getElementById("eye-y-box");
    eyeZBox = document.getElementById("eye-z-box");
    spotXBox = document.getElementById("spot-x-box");
    spotYBox = document.getElementById("spot-y-box");
    spotZBox = document.getElementById("spot-z-box");
    directionXBox = document.getElementById("direction-x-box");
    directionYBox = document.getElementById("direction-y-box");
    directionZBox = document.getElementById("direction-z-box");
    upXBox = document.getElementById("up-x-box");
    upYBox = document.getElementById("up-y-box");
    upZBox = document.getElementById("up-z-box");

    projectionRadio = document.getElementsByName("projection");
    orthographicSection = document.getElementById("orthographic-section");
    perspectiveSection = document.getElementById("perspective-section");

    topBox = document.getElementById("top-box-parallel");
    bottomBox = document.getElementById("bottom-box-parallel");
    leftBox = document.getElementById("left-box-parallel");
    rightBox = document.getElementById("right-box-parallel");
    nearBoxParallel = document.getElementById("near-box-orthographic");
    farBoxParallel = document.getElementById("far-box-orthographic");

    fovBox = document.getElementById("fov-box");
    aspectRatioBox = document.getElementById("aspect-ratio-box");
    nearBoxPerspective = document.getElementById("near-box-perspective");
    farBoxPerspective = document.getElementById("far-box-perspective");

    rxBox3d = document.getElementById("result-x-box-3d");
    ryBox3d = document.getElementById("result-y-box-3d");
    rzBox3d = document.getElementById("result-z-box-3d");

    projectionRadio[0].checked = true;
    orthographicSection.style.display = "block";
    perspectiveSection.style.display = "none";
}

function calculateTarget() {
    var eyeX = parseFloat(eyeXBox.value);
    var eyeY = parseFloat(eyeYBox.value);
    var eyeZ = parseFloat(eyeZBox.value);

    var directionX = parseFloat(directionXBox.value);
    var directionY = parseFloat(directionYBox.value);
    var directionZ = parseFloat(directionZBox.value);

    if (!isNaN(eyeX) && !isNaN(eyeY) && !isNaN(eyeZ) && !isNaN(directionX) && !isNaN(directionY) && !isNaN(directionZ)) {

        var spotX = eyeX - directionX;
        var spotY = eyeY - directionY;
        var spotZ = eyeZ - directionZ;

        spotXBox.value = spotX;
        spotYBox.value = spotY;
        spotZBox.value = spotZ;
    } else {
        spotXBox.value = "";
        spotYBox.value = "";
        spotZBox.value = "";
    }
    canCalculate();
}

function calculateOrientation() {
    var eyeX = parseFloat(eyeXBox.value);
    var eyeY = parseFloat(eyeYBox.value);
    var eyeZ = parseFloat(eyeZBox.value);

    var spotX = parseFloat(spotXBox.value);
    var spotY = parseFloat(spotYBox.value);
    var spotZ = parseFloat(spotZBox.value);

    if (!isNaN(eyeX) && !isNaN(eyeY) && !isNaN(eyeZ) && !isNaN(spotX) && !isNaN(spotY) && !isNaN(spotZ)) {

        var directionX = eyeX - spotX;
        var directionY = eyeY - spotY;
        var directionZ = eyeZ - spotZ;

        var directionVectorNormalized = normalize([directionX, directionY, directionZ]);

        directionXBox.value = directionVectorNormalized[0];
        directionYBox.value = directionVectorNormalized[1];
        directionZBox.value = directionVectorNormalized[2];
    } else {
        directionXBox.value = "";
        directionYBox.value = "";
        directionZBox.value = "";
    }
    canCalculate();
}

function normalizeDirectionVector() {
    var directionX = parseFloat(directionXBox.value);
    var directionY = parseFloat(directionYBox.value);
    var directionZ = parseFloat(directionZBox.value);

    if (!isNaN(directionX) && !isNaN(directionY) && !isNaN(directionZ)) {
        var directionVectorNormalized = normalize([directionX, directionY, directionZ]);

        directionXBox.value = directionVectorNormalized[0];
        directionYBox.value = directionVectorNormalized[1];
        directionZBox.value = directionVectorNormalized[2];
    } else {
        directionXBox.value = "";
        directionYBox.value = "";
        directionZBox.value = "";
    }
    calculateTarget();
}

function normalizeUpVector() {
    var upX = parseFloat(upXBox.value);
    var upY = parseFloat(upYBox.value);
    var upZ = parseFloat(upZBox.value);

    if (!isNaN(upX) && !isNaN(upY) && !isNaN(upZ)) {
        var upVectorNormalized = normalize([upX, upY, upZ]);

        upXBox.value = upVectorNormalized[0];
        upYBox.value = upVectorNormalized[1];
        upZBox.value = upVectorNormalized[2];
    } else {
        upXBox.value = "";
        upYBox.value = "";
        upZBox.value = "";
    }
    canCalculate();
}

function canCalculate() {
    var px = parseFloat(pxBox.value);
    var py = parseFloat(pyBox.value);
    var pz = parseFloat(pzBox.value);
    var eyeX = parseFloat(eyeXBox.value);
    var eyeY = parseFloat(eyeYBox.value);
    var eyeZ = parseFloat(eyeZBox.value);
    var spotX = parseFloat(spotXBox.value);
    var spotY = parseFloat(spotYBox.value);
    var spotZ = parseFloat(spotZBox.value);
    var upX = parseFloat(upXBox.value);
    var upY = parseFloat(upYBox.value);
    var upZ = parseFloat(upZBox.value);

    var pointParametersComplete = !isNaN(px) && !isNaN(py) && !isNaN(pz);
    var viewMatrixParametersComplete = !isNaN(eyeX) && !isNaN(eyeY) && !isNaN(eyeZ) && !isNaN(spotX) && !isNaN(spotY) && !isNaN(spotZ) && !isNaN(upX) && !isNaN(upY) && !isNaN(upZ);
    var projectionParametersComplete;

    if (projectionRadio[0].checked) {
        orthographicSection.style.display = "block";
        perspectiveSection.style.display = "none";

        var top = parseFloat(topBox.value);
        var bottom = parseFloat(bottomBox.value);
        var left = parseFloat(leftBox.value);
        var right = parseFloat(rightBox.value);
        var near = parseFloat(nearBoxParallel.value);
        var far = parseFloat(farBoxParallel.value);

        projectionParametersComplete = !isNaN(top) && !isNaN(bottom) && !isNaN(left) && !isNaN(right) && !isNaN(near) && !isNaN(far);
    } else {
        orthographicSection.style.display = "none";
        perspectiveSection.style.display = "block";

        var fov = parseFloat(fovBox.value);
        var aspectRatio = parseFloat(aspectRatioBox.value);
        var near = parseFloat(nearBoxPerspective.value);
        var far = parseFloat(farBoxPerspective.value);

        projectionParametersComplete = !isNaN(fov) && !isNaN(aspectRatio) && !isNaN(near) && !isNaN(far);
    }

    if (pointParametersComplete && viewMatrixParametersComplete && projectionParametersComplete) {
        var point = math.matrix([[px], [py], [pz], [1]]);
        var viewMatrix = cameraViewMatrix(eyeX, eyeY, eyeZ, spotX, spotY, spotZ, upX, upY, upZ);
        var projectionMatrix;

        if (projectionRadio[0].checked) {
            projectionMatrix = orthographicProjectionMatrix(top, bottom, left, right, near, far);
        } else {
            projectionMatrix = perspectiveProjectionMatrix(fov, aspectRatio, near, far);
        }

        calculate(point, viewMatrix, projectionMatrix);
    } else {
        rxBox3d.value = "";
        ryBox3d.value = "";
        rzBox3d.value = "";
    }
}

function calculate(point, viewMatrix, projectionMatrix) {
    var cameraMatrix = math.multiply(projectionMatrix, viewMatrix);
    var result = math.multiply(cameraMatrix, point);
    var ndcResult = math.multiply(result, -1 / result._data[3][0]);

    rxBox3d.value = ndcResult._data[0][0];
    ryBox3d.value = ndcResult._data[1][0];
    rzBox3d.value = ndcResult._data[2][0];
}

function cameraViewMatrix(eyeX, eyeY, eyeZ, spotX, spotY, spotZ, upX, upY, upZ) {
    var eye = [eyeX, eyeY, eyeZ];
    var spot = [spotX, spotY, spotZ];
    var worldUp = [upX, upY, upZ];
    
    var look = normalize(math.subtract(spot, eye));
    var right = normalize(math.cross(look, worldUp));
    var up = normalize(math.cross(right, look));

    var viewMatrix = [
        [right[0], right[1], right[2], -math.dot(right, eye)],
        [up[0], up[1], up[2], -math.dot(up, eye)],
        [look[0], look[1], look[2], -math.dot(look, eye)],
        [0, 0, 0, 1]
    ];

    return math.matrix(viewMatrix);
}

function orthographicProjectionMatrix(topPlane, bottomPlane, leftPlane, rightPlane, nearPlane, farPlane) {
    var projectionMatrix = [
        [2 / (rightPlane - leftPlane), 0, 0, -(rightPlane + leftPlane) / (rightPlane - leftPlane)],
        [0, 2 / (topPlane - bottomPlane), 0, -(topPlane + bottomPlane) / (topPlane - bottomPlane)],
        [0, 0, -2 / (farPlane - nearPlane), -(farPlane + nearPlane) / (farPlane - nearPlane)],
        [0, 0, 0, 1]
    ];

    return math.matrix(projectionMatrix);
}

function perspectiveProjectionMatrix(fov, aspectRatio, nearPlane, farPlane) {
    fov = fov * Math.PI / 180;

    var topPlane = nearPlane * Math.tan(fov / 2);
    var bottomPlane = -topPlane;
    var rightPlane = topPlane * aspectRatio;
    var leftPlane = -rightPlane;

    var projectionMatrix = [
        [(2 * nearPlane) / (rightPlane - leftPlane), 0, (rightPlane + leftPlane) / (rightPlane - leftPlane), 0],
        [0, (2 * nearPlane) / (topPlane - bottomPlane), (topPlane + bottomPlane) / (topPlane - bottomPlane), 0],
        [0, 0, -(farPlane + nearPlane) / (farPlane - nearPlane), -(2 * farPlane * nearPlane) / (farPlane - nearPlane)],
        [0, 0, -1, 0]
    ];

    return math.matrix(projectionMatrix);
}

function normalize(vector) {
    var magnitude = math.sqrt(math.square(vector[0]) + math.square(vector[1]) + math.square(vector[2]));
    return math.dotMultiply(vector, [1 / magnitude, 1 / magnitude, 1 / magnitude]);
}