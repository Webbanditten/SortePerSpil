<%--
  Created by IntelliJ IDEA.
  User: patrick-wohlk
  Date: 06/05/15
  Time: 09.29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
  <script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script>
    var path;
    var url = "http://192.168.87.111:8080/SorteperWeb";
    var spiller = {
      blandKort: function() {
        $.get(url + "/bland", null, function(data) { spiller.mineKort(); })
      },
      mineKort: function() {
        $.get(url + "/minekort", null, function(data) {
          if($(data).find("response[type='error']").text() != "") {
            $("#mineKort .kortListe").text($(data).find("response").text());
          }else {
            // Fjern alle mine kort fra view
            $("#mineKort .kortListe").empty();

            // tilføj dem igen
            $(data).find('kort').each(function () {
              //<kuloer>RUDER</kuloer>
              //<vaerdi>9</vaerdi>
              var kuloer, vaerdi;
              kuloer = spil.getKulorIcon($(this).find("kuloer").text());
              vaerdi = $(this).find("vaerdi").text();

              $("#mineKort .kortListe").append("<li><small>" + vaerdi + "</small><strong>" + kuloer + "</strong></li>");
            });
          }
        });
      },
      tilfojSpiller: function(navn) {
        $.get(url + "/tilfojspiller", { navn:navn }, function(data) { spil.viewMainUI(); });

      },
      findSpillere: function() {
        $.get(url + "/listspillere", null, function(data) {
          $("#spillerliste").empty();
          $(data).find('spiller').each(function(){
            var readyIcon, navn, klar, antalKort;
            readyIcon = "✗";
            navn = $(this).find("navn").text();
            klar = $(this).find("klar").text();
            antalKort = $(this).find("antalKort").text();

            if($(this).find("klar").text() == "true") {
              readyIcon = "✓";
            }
            $("#spillerliste").append("<li>" + navn + " ("+readyIcon+") - Kort: "+ antalKort +"</li>");
          });
        });
      },
      tilfojSpillerDialog: function() {
        var person = prompt("Indtast navn", "Unknown user");
        if (person != null) {
          spiller.tilfojSpiller(person);
          localStorage.setItem("navn", person);
        }else{
          spiller.tilfojSpillerDialog();
        }
      },
      meldKlar: function() {
        $.get(url + "/klar", null, function(data) {
          localStorage.setItem("ready", true);
          $("#ready").hide();
          spil.viewMainUI();
        });
      },
      tagKort: function(index) {
        $.get(url + "/tagkort", { card: index }, function() {
          $("#clientAction").empty().show();
          $("#clientAction").text("Du har taget et kort!").fadeOut(2000)
          spil.viewMainUI();
        });
      }
    }

    var spil = {
      modstanderKort: function() {
        $.get(url + "/modstanderkort", null, function(data) {
          if($(data).find("response[type='error']").text() != "") {
            $("#modstanderKort .kortListe").text($(data).find("response").text());
          }else{
            var antalkort = $(data).find("antalKort").text();
            $("#modstanderKort .kortListe").empty();

            for(i = 0; i < antalkort; i++) {
              $("#modstanderKort .kortListe").append("<li data-cardid='"+i+"'><strong>★</strong></li>");
            }
          }


        });
      },
      findPar: function() {
        $.get(url + "/findpar", null, function()  {
          spil.viewMainUI();
        });
      },
      getKulorIcon: function(kulor) {
        var icon = null;
        switch(kulor) {
          case "RUDER":
            icon = "♦";
            break;
          case "SPAR":
            icon = "♠";
            break;
          case "KLOER":
            icon = "♣";
            break;
          case "HJERTER":
            icon = "♥";
            break;
          case "JOKER":
            icon = "✪";
            break;
          default:
            icon = "N/A";
            break;
        }
        return icon;
      },
      getStatus: function() {
        // TODO Ajax kald til serveren en form for status isde
        $.get(url + "/status", null, function(data) {
          $("#status").text($(data).find("status").text());
        });
        $("#status").text("Venter på alle spillere");
      },
      viewMainUI: function() {
        $("#mainUI").show();
        spiller.findSpillere();
        spil.getStatus();

        // Fjern klar knap
        if(localStorage.getItem("ready") && localStorage.getItem("ready") == "true") {
          $("#ready").hide();

          spiller.mineKort();
          spil.modstanderKort();

        }
      },
      boot: function() {
        if(localStorage.getItem("navn")) {
          spil.viewMainUI();

        }else{
          spiller.tilfojSpillerDialog();
        }
      }
    }

    function totalReset() {
      localStorage.removeItem("navn");
      localStorage.removeItem("ready");
    }




    $(document).ready(function(e) {
      spil.boot();

      setInterval(function () {spil.viewMainUI();}, 5000);

      $("#modstanderKort").on("click", ".kortListe li", function() {
        spiller.tagKort($(this).data("cardid"));
      });
    });
  </script>


  <style>
    #clientAction {
      font-weight: bold;
      font-size:16px;
      padding-bottom:5px;
    }
    #mainUI {
      width:800px;
      margin:0 auto;
      display:none;
    }
    #ready {

    }
    #left {
      width:400px;
      float:left;
    }
    #right { width:400px; float:right; }
    .kortListe {
      list-style:none;
      width:350px;
    }
    .kortListe li {
      border:1px solid #000;
      width:30px;
      height:40px;
      display:inline-block;
      margin-top:3px;
    }
    #mineKort .kortListe li strong {
      font-size: 20px;
      position: relative;
      top: 11px;
      left: 0px;
    }
    #modstanderKort .kortListe li strong {
      font-size: 20px;
      position: relative;
      top: 5px;
      left: 4px;
    }
    #mineKort .kortListe li small {
      position: relative;
      left: 18px;
      top: -5px;
    }
  </style>
</head>
<body>
<div id="mainUI">
  <div id="left">
    <div id="clientAction"></div>
    <div id="status"></div>
    <div id="modstanderKort">
      <h3>Modstanders kort</h3>
      <ul class="kortListe">

      </ul>
    </div>
    <div id="mineKort">
      <h3>Dine Kort</h3>
      <ul class="kortListe"></ul>
      <input type="button" value="Bland dine kort" onclick="spiller.blandKort();">
      <input type="button" value="Fjern par" onClick="spil.findPar();">
    </div>
  </div>
  <div id="right">
    <div id="ready"><input type="button" value="Klar?" onClick="spiller.meldKlar();"></div>
    <strong>Spillere</strong>
    <ul id="spillerliste">
      <li>Loader spillere</li>

    </ul>
  </div>
</div>

<input type="button" value="Total reset - Dev only" onClick="totalReset();">
</body>
</html>