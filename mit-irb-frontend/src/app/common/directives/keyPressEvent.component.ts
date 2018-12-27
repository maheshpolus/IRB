declare var $: any;

export class KeyPressEvent {


    /* keypress event*/
    onKeyDown(e, id) {
        const code = e.keyCode || e.which;

        if ((code !== 32) && (code !== 40) && (code !== 38) && (code !== 13)) {
            // Do something Deactivate on space
        } else if (code === 40) { // Down arrow
            if ($('#' + id + ' li.selected').index() === $('#' + id + ' li').length - 1) {
                $('#' + id + ' li.selected').removeClass('selected');
            }
            if ($('#' + id + ' li.selected').length === 0) { // if no li has the selected class
                $('#' + id + ' li').eq(0).addClass('selected');
            } else {
                $('#' + id + ' li.selected').eq(0).removeClass('selected').next().addClass('selected');
            }
        } else if (code === 38) { // Up arrow
            if ($('#' + id + ' li.selected').index() === 0) {
                return;
            }

            if (!$('#' + id + ' li.selected')) { // if no li has the selected class
                $('#' + id + ' li.selected').eq(0).removeClass('selected');
            } else {
                $('#' + id + ' li.selected').eq(0).removeClass('selected').prev().addClass('selected');
            }
        }
    }
}
