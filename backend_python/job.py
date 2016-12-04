import webapp2
from google.appengine.ext import ndb
import db_models
import json




class Job(webapp2.RequestHandler):
    def post(self):
        """Creates a Job entity
        Properties of the JSON
        POST Body Variables:
        caption - String.
        street - String
        city - String 
        zip_code - String
        drop - String - describes where to take items
        offer - Integer - details how much a job pays
        """
        # Only accepts JSON
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/json MIME type"
            return
        new_job = db_models.Job()
        caption = self.request.get('caption', default_value=None)
        street = self.request.get('street', default_value=None)
        city = self.request.get('city', default_value=None)
        zip_code = self.request.get('zip_code', default_value=None)
        drop = self.request.get('drop', default_value=None)
        offer = self.request.get('offer', default_value=None)
        if caption:
            new_job.caption = caption
        else:
            self.response.status = 400
            self.response.status_message = "Invalid request, Job Caption is Required."
        if street:
            new_job.street = street
        if city:
            new_job.city = city
        if zip_code:
            new_job.zip_code = zip_code
        if drop:
            new_job.drop = drop
        if offer:
            new_job.offer = offer
        key = new_job.put()
        out = new_job.to_dict()
        self.response.write(json.dumps(out))
        return

    # Search and retrieve
    def get(self, **kwargs):
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/json MIME type"
            return
        # check id in the kwargs
        if 'id' in kwargs:
            # create a key from the id passed in, you get the job, and then turn it to a dictionary
            out = ndb.Key(db_models.Job, int(kwargs['id'])).get().to_dict()
            # dumps it to a json string and sends it back
            self.response.write(json.dumps(out))
        # and if no id, returns all keys in a similar way
        else:
            q = db_models.Job.query()
            keys = q.fetch()
            # results = {'caption': [x.caption for x in keys]}
            # self.response.write(json.dumps(q))
            keys = q.fetch(keys_only=True)
            results = {'job keys': [x.id() for x in keys]}
            # keys = q.fetch()
            # results2 = {'Zip Codes with Jobs': [x.zip_code for x in keys]}
            # keys = q.fetch()
            # results3 = {'Captions': [x.caption for x in keys], "key": [x.id() for x in keys]}
            self.response.write(json.dumps(results))
            # self.response.write("<br />")
            # self.response.write(json.dumps(results))
            # self.response.write("<br />")
            # self.response.write(json.dumps(results2))


    def delete(self, **kwargs):
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/json MIME type"
            return
        if 'id' in kwargs:
            toDel = ndb.Key(db_models.Job, int(kwargs['id'])).get()
            toDel.key.delete()
            return
        else:
            q = db_models.Job.query()
            keys = q.fetch(keys_only=True)
            results = {'keys': [x.id() for x in keys]}
            self.response.write(json.dumps(results))
            return


class JobSearch(webapp2.RequestHandler):
    # changed to GET from POST - only retrieves a list of keys -> POST method not necessary
    def get(self):
        """
        Search mechanisms 
        GET Body Variables:
        caption - String.  
        zip_code - String.
        """
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/json MIME type"
            return
        q = db_models.Job.query()
        if self.request.get('caption', None):
            q = q.filter(db_models.Job.caption == self.request.get('caption'))
        if self.request.get('zip_code', None):
            q = q.filter(db_models.Job.zip_code == self.request.get('zip_code'))
        keys = q.fetch(keys_only=True)
        results = {'keys': [x.id() for x in keys]}
        self.response.write(json.dumps(results))
