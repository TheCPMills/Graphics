var pxBox, pyBox, pzBox, vxBox, vyBox, vzBox, tBox, tRadio, rxBox, ryBox, rzBox;

function setup() {
    pxBox = document.getElementById("px-box");
    pyBox = document.getElementById("py-box");
    pzBox = document.getElementById("pz-box");
    vxBox = document.getElementById("vx-box");
    vyBox = document.getElementById("vy-box");
    vzBox = document.getElementById("vz-box");
    tBox = document.getElementById("t-box");

    tRadio = document.getElementsByName("angle");

    rxBox = document.getElementById("result-x-box");
    ryBox = document.getElementById("result-y-box");
    rzBox = document.getElementById("result-z-box");
}


function canCalculate() {
    var px = parseFloat(pxBox.value);
    var py = parseFloat(pyBox.value);
    var pz = parseFloat(pzBox.value);
    var vx = parseFloat(vxBox.value);
    var vy = parseFloat(vyBox.value);
    var vz = parseFloat(vzBox.value);
    var t = parseFloat(tBox.value);

    if (!isNaN(px) && !isNaN(py) && !isNaN(pz) && !isNaN(vx) && !isNaN(vy) && !isNaN(vz) && !isNaN(t)) {
        if (tRadio[0].checked) {
            var theta = t * Math.PI / 180;
        } else {
            var theta = t;
        }
        var result = rotate(px, py, pz, vx, vy, vz, theta);

        rxBox.value = result[0];
        ryBox.value = result[1];
        rzBox.value = result[2];
    } else {
        rxBox.value = "result x-value";
        ryBox.value = "result y-value";
        rzBox.value = "result z-value";
    }
}

function rotate(px, py, pz, vx, vy, vz, theta) {
    // normalize the vector defining the axis of rotation
    var vectorLength = Math.sqrt(vx * vx + vy * vy + vz * vz);
    vx /= vectorLength;
    vy /= vectorLength;
    vz /= vectorLength;

    // compute the x coordinate of the rotated point
    var i = px * Math.pow(Math.sin(theta / 2), 2) * (Math.pow(vx, 2) - Math.pow(vy, 2) - Math.pow(vz, 2))
          + px * Math.pow(Math.cos(theta / 2), 2)
          - vz * py * Math.sin(theta)
          + 2 * vx * vy * py * Math.pow(Math.sin(theta / 2), 2)
          + vy * pz * Math.sin(theta)
          + 2 * vx * vz * pz * Math.pow(Math.sin(theta / 2), 2);

    // compute the y coordinate of the rotated point
    var j = vz * px * Math.sin(theta)
          + 2 * vx * vy * px * Math.pow(Math.sin(theta / 2), 2)
          + py * Math.pow(Math.sin(theta / 2), 2) * (Math.pow(vy, 2) - Math.pow(vx, 2) - Math.pow(vz, 2))
          + py * Math.pow(Math.cos(theta / 2), 2)
          - vx * pz * Math.sin(theta)
          + 2 * vy * vz * pz * Math.pow(Math.sin(theta / 2), 2);

    // compute the z coordinate of the rotated point
    var k = - vy * px * Math.sin(theta)
            + 2 * vx * vz * px * Math.pow(Math.sin(theta / 2), 2)
            + vx * py * Math.sin(theta)
            + 2 * vy * vz * py * Math.pow(Math.sin(theta / 2), 2)
            + pz * Math.pow(Math.sin(theta / 2), 2) * (Math.pow(vz, 2) - Math.pow(vx, 2) - Math.pow(vy, 2))
            + pz * Math.pow(Math.cos(theta / 2), 2);

    return [i, j, k];

}