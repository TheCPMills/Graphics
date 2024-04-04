var pxBox, pyBox, pzBox, transformationRadio, translationSection, rotationSection, scalingSection, reflectionSection, shearingSection, txBox, tyBox, tzBox, vxBox, vyBox, vzBox, rxBox, ryBox, rzBox, thetaBox, thetaRadio, sxBox, syBox, szBox, faBox, fbBox, fcBox, fdBox, hxyBox, hxzBox, hyxBox, hyzBox, hzxBox, hzyBox;

function setup() {
    pxBox = document.getElementById("px-box");
    pyBox = document.getElementById("py-box");
    pzBox = document.getElementById("pz-box");

    transformationRadio = document.getElementsByName("transformation");
    translationSection = document.getElementById("translation-section");
    rotationSection = document.getElementById("rotation-section");
    scalingSection = document.getElementById("scaling-section");
    reflectionSection = document.getElementById("reflection-section");
    shearingSection = document.getElementById("shearing-section");

    txBox = document.getElementById("tx-box");
    tyBox = document.getElementById("ty-box");
    tzBox = document.getElementById("tz-box");

    vxBox = document.getElementById("vx-box");
    vyBox = document.getElementById("vy-box");
    vzBox = document.getElementById("vz-box");
    thetaBox = document.getElementById("theta-box");
    thetaRadio = document.getElementsByName("angle");

    sxBox = document.getElementById("sx-box");
    syBox = document.getElementById("sy-box");
    szBox = document.getElementById("sz-box");

    faBox = document.getElementById("fa-box");
    fbBox = document.getElementById("fb-box");
    fcBox = document.getElementById("fc-box");
    fdBox = document.getElementById("fd-box");

    hxyBox = document.getElementById("hxy-box");
    hxzBox = document.getElementById("hxz-box");
    hyxBox = document.getElementById("hyx-box");
    hyzBox = document.getElementById("hyz-box");
    hzxBox = document.getElementById("hzx-box");
    hzyBox = document.getElementById("hzy-box");

    rxBox = document.getElementById("result-x-box");
    ryBox = document.getElementById("result-y-box");
    rzBox = document.getElementById("result-z-box");

    transformationRadio[0].checked = true;
    translationSection.style.display = "block";
    rotationSection.style.display = "none";
    scalingSection.style.display = "none";
    reflectionSection.style.display = "none";
    shearingSection.style.display = "none";
}

function canCalculate() {
    var px = parseFloat(pxBox.value);
    var py = parseFloat(pyBox.value);
    var pz = parseFloat(pzBox.value);

    var pointParametersComplete = !isNaN(px) && !isNaN(py) && !isNaN(pz);
    var transformParametersComplete;

    if (transformationRadio[0].checked) {
        translationSection.style.display = "block";
        rotationSection.style.display = "none";
        scalingSection.style.display = "none";
        reflectionSection.style.display = "none";
        shearingSection.style.display = "none";

        var tx = parseFloat(txBox.value);
        var ty = parseFloat(tyBox.value);
        var tz = parseFloat(tzBox.value);

        transformParametersComplete = !isNaN(tx) && !isNaN(ty) && !isNaN(tz);
    } else if (transformationRadio[1].checked) {
        translationSection.style.display = "none";
        rotationSection.style.display = "block";
        scalingSection.style.display = "none";
        reflectionSection.style.display = "none";
        shearingSection.style.display = "none";

        var vx = parseFloat(vxBox.value);
        var vy = parseFloat(vyBox.value);
        var vz = parseFloat(vzBox.value);
        var theta = parseFloat(thetaBox.value);

        if (thetaRadio[0].checked) {
            var angle = theta * Math.PI / 180; 
        } else {
            var angle = theta;
        }

        transformParametersComplete = !isNaN(vx) && !isNaN(vy) && !isNaN(vz) && !isNaN(angle);
    } else if (transformationRadio[2].checked) {
        translationSection.style.display = "none";
        rotationSection.style.display = "none";
        scalingSection.style.display = "block";
        reflectionSection.style.display = "none";
        shearingSection.style.display = "none";

        var sx = parseFloat(sxBox.value);
        var sy = parseFloat(syBox.value);
        var sz = parseFloat(szBox.value);

        transformParametersComplete = !isNaN(sx) && !isNaN(sy) && !isNaN(sz);
    } else if (transformationRadio[3].checked) {
        translationSection.style.display = "none";
        rotationSection.style.display = "none";
        scalingSection.style.display = "none";
        reflectionSection.style.display = "block";
        shearingSection.style.display = "none";

        var fa = parseFloat(faBox.value);
        var fb = parseFloat(fbBox.value);
        var fc = parseFloat(fcBox.value);
        var fd = parseFloat(fdBox.value);

        transformParametersComplete = !isNaN(fa) && !isNaN(fb) && !isNaN(fc) && !isNaN(fd);
    } else {
        translationSection.style.display = "none";
        rotationSection.style.display = "none";
        scalingSection.style.display = "none";
        reflectionSection.style.display = "none";
        shearingSection.style.display = "block";

        var hxy = parseFloat(hxyBox.value);
        var hxz = parseFloat(hxzBox.value);
        var hyx = parseFloat(hyxBox.value);
        var hyz = parseFloat(hyzBox.value);
        var hzx = parseFloat(hzxBox.value);
        var hzy = parseFloat(hzyBox.value);

        transformParametersComplete = !isNaN(hxy) && !isNaN(hxz) && !isNaN(hyx) && !isNaN(hyz) && !isNaN(hzx) && !isNaN(hzy);
    }

    if (pointParametersComplete && transformParametersComplete) {
        var point = math.matrix([[px], [py], [pz], [1]]);
        var transformationMatrix;

        if (transformationRadio[0].checked) {
            transformationMatrix = translate(tx, ty, tz);
        } else if (transformationRadio[1].checked) {
            transformationMatrix = rotate(vx, vy, vz, angle);
        } else if (transformationRadio[2].checked) {
            transformationMatrix = scale(sx, sy, sz);
        } else if (transformationRadio[3].checked) {
            transformationMatrix = reflect(fa, fb, fc, fd);
        } else {
            transformationMatrix = shear(hxy, hxz, hyx, hyz, hzx, hzy);
        }

        calculate(point, transformationMatrix);
    } else {
        rxBox.value = "";
        ryBox.value = "";
        rzBox.value = "";
    }
}

function calculate(point, transformationMatrix) {
    var result = math.multiply(transformationMatrix, point);

    rxBox.value = result._data[0][0];
    ryBox.value = result._data[1][0];
    rzBox.value = result._data[2][0];
}

function translate(tx, ty, tz) {
    var translationMatrix = [
        [1, 0, 0, tx],
        [0, 1, 0, ty],
        [0, 0, 1, tz],
        [0, 0, 0, 1]
    ];

    return math.matrix(translationMatrix);
}

function rotate(vx, vy, vz, theta) {
    var normalizedVector = normalizeVector([vx, vy, vz]);

    var x = normalizedVector[0];
    var y = normalizedVector[1];
    var z = normalizedVector[2];

    var c = Math.cos(theta);
    var omc = 1.0 - c;
    var s = Math.sin(theta);

    var rotationMatrix = [
        [x * x * omc + c, x * y * omc - z * s, x * z * omc + y * s, 0.0],
        [x * y * omc + z * s, y * y * omc + c, y * z * omc - x * s, 0.0],
        [x * z * omc - y * s, y * z * omc + x * s, z * z * omc + c, 0.0],
        [0.0, 0.0, 0.0, 1.0]
    ];

    return math.matrix(rotationMatrix);
}

function scale(sx, sy, sz) {
    var scaleMatrix = [
        [sx, 0.0, 0.0, 0.0],
        [0.0, sy, 0.0, 0.0],
        [0.0, 0.0, sz, 0.0],
        [0.0, 0.0, 0.0, 1.0]
    ];

    return math.matrix(scaleMatrix);
}

function reflect(fa, fb, fc, fd) {
    var normalizedPlane = normalizePlane([fa, fb, fc, fd]);

    var a = normalizedPlane[0];
    var b = normalizedPlane[1];
    var c = normalizedPlane[2];
    var d = normalizedPlane[3];

    var reflectionMatrix = [
        [1.0 - 2.0 * a * a, -2.0 * a * b, -2.0 * a * c, -2.0 * a * d],
        [-2.0 * a * b, 1.0 - 2.0 * b * b, -2.0 * b * c, -2.0 * b * d],
        [-2.0 * a * c, -2.0 * b * c, 1.0 - 2.0 * c * c, -2.0 * c * d],
        [0.0, 0.0, 0.0, 1.0]
    ];

    return math.matrix(reflectionMatrix);
}

function shear(hxy, hxz, hyx, hyz, hzx, hzy) {
    var shearMatrix = [
        [1.0, hxy, hxz, 0.0],
        [hyx, 1.0, hyz, 0.0],
        [hzx, hzy, 1.0, 0.0],
        [0.0, 0.0, 0.0, 1.0]
    ];

    return math.matrix(shearMatrix);
}

function normalizeVector(vector) {
    var magnitude = math.sqrt(math.square(vector[0]) + math.square(vector[1]) + math.square(vector[2]));
    return math.dotMultiply(vector, [1 / magnitude, 1 / magnitude, 1 / magnitude]);
}

function normalizePlane(plane) {
    var magnitude = math.sqrt(math.square(plane[0]) + math.square(plane[1]) + math.square(plane[2]));
    return math.dotMultiply(plane, [1 / magnitude, 1 / magnitude, 1 / magnitude, 1 / magnitude]);
}