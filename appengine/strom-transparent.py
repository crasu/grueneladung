import webapp2
import json
from google.appengine.ext import db
from google.appengine.api import users
import urllib2
import re
import time

class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.write(json.dumps(getPowerValues()))

def getPowerValues():
    l = []
    pvs = db.GqlQuery("SELECT * FROM PowerValue ORDER BY time DESC")
    for pv in pvs[0:30]:
            d = {"gas": pv.gas, "steinkohle": pv.steinkohle, "braunkohle": pv.braunkohle, "kernenergie": pv.kernenergie}
            l.append(d)
    pvs.time = int(time.time())
    return l            

class Cron(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.write("starting cron ...\n")
        getValuesFromRwe()
        self.response.write("cron executed\n")

def getValuesFromRwe():
    powertypes = ['gas', 'steinkohle', 'braunkohle', 'kernenergie']
    result = urllib2.urlopen("http://www.rwe.com/app/tso/kwl_uebersicht.aspx").read()
    pv = PowerValue()
    for type in powertypes:
        setattr(pv, type, getPowerType(result, type))
    pv.put()

def getPowerType(result, type):
    return int(re.findall("%s gesamt.*?<strong>(.*?)</strong>" % type, result, re.IGNORECASE)[0])

class PowerValue(db.Model):
    gas = db.IntegerProperty()
    braunkohle = db.IntegerProperty()
    steinkohle = db.IntegerProperty()
    kernenergie = db.IntegerProperty()
    time = db.IntegerProperty()

application = webapp2.WSGIApplication([
    ('/power', MainPage),('/cron', Cron) 
], debug=True)

