package mn.tsai.booklo;

public class Filler {

    public static final String JS =
    "(function(){" +
    "var t=window.__ORDER__||'';" +
    "if(!t)return;" +

    "function set(el,val){if(!el)return;" +
    " var s=Object.getOwnPropertyDescriptor(el.__proto__,'value').set;" +
    " s.call(el,val);" +
    " el.dispatchEvent(new Event('input',{bubbles:true}));" +
    " el.dispatchEvent(new Event('change',{bubbles:true}));}" +

    "function byLabel(txt){" +
    " var ls=document.querySelectorAll('label');" +
    " for(var i=0;i<ls.length;i++){" +
    "  if(ls[i].innerText.trim().toLowerCase().indexOf(txt.toLowerCase())===0){" +
    "   var p=ls[i].parentElement;" +
    "   var f=p.querySelector('input,textarea,select');" +
    "   if(f)return f;}}" +
    " return null;}" +

    "var phone=(t.match(/(?:^|\\D)(\\d{8})(?:\\D|$)/)||[])[1]||'';" +

    "var name='';" +
    "var lines=t.split('\\n');" +
    "for(var i=0;i<lines.length;i++){" +
    " var m=lines[i].match(/^\\s*([\\u0400-\\u04FF]{2,20})\\s+\\d{8}/);" +
    " if(m){name=m[1];break;}}" +

    "var addr='';" +
    "for(var i=0;i<lines.length;i++){" +
    " if(/хороо|байр|тоот|хотхон|гудамж/i.test(lines[i])){addr=lines[i].trim();break;}}" +

    "set(byLabel('Хүлээн авагч утас'),phone);" +
    "if(name)set(byLabel('Хүлээн авагч'),name);" +
    "if(addr)set(byLabel('Хаяг'),addr);" +

    "var sels=document.querySelectorAll('select');" +
    "var dist=(t.match(/(ХУД|БЗД|СБД|ЧД|БГД|СХД|БХД|Хан-Уул|Баянзүрх|Сүхбаатар|Чингэлтэй|Баянгол|Сонгинохайрхан)/i)||[])[1];" +
    "var horoo=(t.match(/(\\d{1,2})\\s*-?\\s*р\\s*хороо/i)||[])[1];" +

    "function pick(sel,txt){if(!sel||!txt)return;" +
    " for(var i=0;i<sel.options.length;i++){" +
    "  if(sel.options[i].text.toLowerCase().indexOf(txt.toLowerCase())>=0){" +
    "   sel.selectedIndex=i;" +
    "   sel.dispatchEvent(new Event('change',{bubbles:true}));return;}}}" +

    "if(dist&&sels.length>0)pick(sels[0],dist);" +
    "if(horoo&&sels.length>1)setTimeout(function(){pick(sels[1],horoo+'-р хороо');},800);" +

    "var rows=document.querySelectorAll('select');" +
    "var low=t.toLowerCase();" +
    "for(var i=0;i<rows.length;i++){" +
    " var box=rows[i].closest('div');" +
    " if(!box)continue;" +
    " var label=box.innerText.toLowerCase();" +
    " var key=label.split('(')[0].trim();" +
    " if(key.length<4)continue;" +
    " var words=key.split(/\\s+/);" +
    " var hit=null;" +
    " for(var w=0;w<words.length;w++){" +
    "  if(words[w].length<4)continue;" +
    "  var re=new RegExp(words[w]+'[^\\\\n]{0,30}?(\\\\d+)','i');" +
    "  var m=t.match(re);" +
    "  if(m){hit=m[1];break;}}" +
    " if(hit)pick(rows[i],hit);}" +

    "})();";
}
