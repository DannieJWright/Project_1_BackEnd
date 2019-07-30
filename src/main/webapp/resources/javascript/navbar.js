
'use strict';

init();

/**
 * To work, need to have an element with id="navParent" to append the navbar to
 */

function init () {
    let parent = document.getElementById ("navParent");
    let nav = document.createElement ("ul");
    nav.setAttribute ("class","nav nav-pills nav-stacked");

    //add revature logo at very top
    let logo = document.createElement ("img");
    logo.src = "../resources/revaturelogo.png";
    logo.style = "width:100%";
    nav.appendChild (logo);

    //add a line break at the top of the nav
    nav.appendChild (document.createElement ("br"));

    //create all of the path options
    nav.appendChild (getOption ("#home","Home"));
    nav.appendChild (getOption ("#pastReq", "Past Requests"));
    nav.appendChild (getOption ("#makeReq", "Make Request"));

    //append navbar to it's parent
    parent.appendChild (nav);
}

function getOption (link, title) {
    let l = document.createElement ("li");
    let a = document.createElement ("a");
    a.href = link;
    a.innerHTML = title;
    l.appendChild (a);
    return l;
}
