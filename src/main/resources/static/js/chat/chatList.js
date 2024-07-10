document.addEventListener('DOMContentLoaded', function() {
    Hover();
});

function Hover(){
    // 1. 배경색, 글자색 hover
    $('.chat-wrapper').on('mouseenter', '.chat-li-content', function (e){
        $(this).css('backgroundColor', '#F1F5F6');
        $(this).find('.conversation').css('color', '#5AB6D9');
    }).on('mouseleave', '.chat-li-content', function (){
        $(this).css('backgroundColor', '');
        $(this).find('.conversation').css('color', '');
    });

    // 2. 버튼 호버
    $('.next-wrap').on('mouseenter', function (){
        $(this).css({
            'backgroundColor' : 'white',
            'transition-duration' : '0.3s'
        });
        $(this).find('.submit-default').css('display', 'none');
        $(this).find('.hovers').css('display', 'inline-block');

    }).on('mouseleave', function (){
        $(this).css({
            'backgroundColor' : '',
            'transition-duration' : '0.2s'
        });
        $(this).find('.submit-default').css('display', '');
        $(this).find('.hovers').css('display', 'none');

    })
}
