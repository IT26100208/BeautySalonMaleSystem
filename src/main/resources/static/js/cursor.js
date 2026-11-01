/* Bella Aura - scissors cursor with a glowing purple-pink trail.
   Only runs on devices with a real mouse. */
(function () {
    if (!window.matchMedia('(hover: hover) and (pointer: fine)').matches) return;

    var reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    var LIFE = 450;                       // trail lifetime in ms
    var body = document.body;
    body.classList.add('has-scissors');

    /* ---- scissors element ---- */
    var blade =
        '<polygon points="20,17.8 41,20 20,22.2" fill="#fff"/>' +
        '<line x1="20" y1="20" x2="9" y2="20" stroke="url(#scG)" stroke-width="2.4" stroke-linecap="round"/>' +
        '<circle cx="5.5" cy="20" r="3.6" fill="none" stroke="url(#scG)" stroke-width="2.4"/>';

    var el = document.createElement('div');
    el.className = 'scissors-cursor is-hidden';
    el.innerHTML =
        '<svg width="44" height="40" viewBox="0 0 44 40" aria-hidden="true">' +
        '<defs><linearGradient id="scG" x1="0" y1="0" x2="1" y2="1">' +
        '<stop offset="0" stop-color="#a78bfa"/><stop offset="1" stop-color="#f472b6"/>' +
        '</linearGradient></defs>' +
        '<g id="scA">' + blade + '</g><g id="scB">' + blade + '</g>' +
        '<circle cx="20" cy="20" r="1.9" fill="#ec4899"/></svg>';
    body.appendChild(el);
    var bladeA = el.querySelector('#scA');
    var bladeB = el.querySelector('#scB');

    /* ---- trail canvas ---- */
    var canvas, ctx, dpr = 1;
    if (!reduceMotion) {
        canvas = document.createElement('canvas');
        canvas.className = 'cursor-trail';
        body.appendChild(canvas);
        ctx = canvas.getContext('2d');
        var resize = function () {
            dpr = window.devicePixelRatio || 1;
            canvas.width = innerWidth * dpr;
            canvas.height = innerHeight * dpr;
            ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
        };
        resize();
        addEventListener('resize', resize);
    }

    /* ---- state ---- */
    var mx = -100, my = -100, lx = null, ly = null;
    var ang = -135, tAng = -135;          // idle: tip points up-left like a normal cursor
    var open = 14, tOpen = 14;            // blade opening angle in degrees
    var pressed = false, overLink = false, points = [];

    addEventListener('mousemove', function (e) {
        mx = e.clientX; my = e.clientY;
        if (lx !== null) {
            var dx = mx - lx, dy = my - ly;
            if (dx * dx + dy * dy > 16) {
                tAng = Math.atan2(dy, dx) * 180 / Math.PI;   // tip leads the motion
                lx = mx; ly = my;
            }
        } else { lx = mx; ly = my; }

        var t = e.target;
        var overText = !!(t.closest && t.closest('input, textarea'));
        overLink = !!(t.closest && t.closest('a, button, label, select, [role="button"]'));
        el.classList.toggle('is-hidden', overText);   // native caret takes over in fields

        if (canvas) points.push({ x: mx, y: my, t: performance.now() });
    }, { passive: true });

    addEventListener('mousedown', function () { pressed = true; });
    addEventListener('mouseup',   function () { pressed = false; });
    document.documentElement.addEventListener('mouseleave', function () { el.classList.add('is-hidden'); });
    document.documentElement.addEventListener('mouseenter', function () { el.classList.remove('is-hidden'); });

    /* ---- render loop ---- */
    function frame(now) {
        // smooth rotation along the shortest path
        var diff = ((tAng - ang + 540) % 360) - 180;
        ang += diff * 0.18;

        // blades: snip shut on click, open wider over buttons and links
        tOpen = pressed ? 0 : (overLink ? 30 : 14);
        open += (tOpen - open) * 0.35;

        el.style.transform = 'translate(' + (mx - 41) + 'px,' + (my - 20) + 'px) rotate(' + ang + 'deg)';
        bladeA.setAttribute('transform', 'rotate(' + open + ' 20 20)');
        bladeB.setAttribute('transform', 'rotate(' + (-open) + ' 20 20)');

        if (ctx) {
            ctx.clearRect(0, 0, innerWidth, innerHeight);
            points = points.filter(function (p) { return now - p.t < LIFE; });
            ctx.lineCap = 'round';
            ctx.lineJoin = 'round';
            for (var i = 1; i < points.length; i++) {
                var a = points[i - 1], b = points[i];
                var age = (now - b.t) / LIFE;                 // 0 new -> 1 old
                var color = 'hsla(' + (330 - 60 * age) + ',90%,65%,' + (1 - age) + ')';
                ctx.strokeStyle = color;
                ctx.shadowColor = color;
                ctx.shadowBlur = 14;
                ctx.lineWidth = 7 * (1 - age) + 0.5;
                ctx.beginPath();
                ctx.moveTo(a.x, a.y);
                ctx.lineTo(b.x, b.y);
                ctx.stroke();
            }
        }
        requestAnimationFrame(frame);
    }
    requestAnimationFrame(frame);
})();