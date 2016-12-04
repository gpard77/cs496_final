import webapp2
from google.appengine.ext import ndb
import db_models
import json


class Member(webapp2.RequestHandler):
    def post(self):
        """Creates a Member entity
        POST Body Variables:
        user_name - Required.  Member user_name
        passcode - Required. Member password
        first_name - String. Member first_name
        last_name - String. Member last_name
        jobs[] - Array of job ids
        """
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/JSON"
            return
        new_member = db_models.Member()
        user_name = self.request.get('user_name', default_value=None)
        passcode = self.request.get('passcode', default_value=None)
        first_name = self.request.get('first_name', default_value=None)
        last_name = self.request.get('last_name', default_value=None)
        jobs = self.request.get_all('jobs[]', default_value=None)
        # title normally required - pass for this week - TODO -> require and update with error status
        if user_name:
            new_member.user_name = user_name
        # else:
        #     self.response.status = 400
        #     self.response.status_message = "Invalid request: Name needed!"
        if passcode:
            new_member.passcode = passcode
        if first_name:
            new_member.first_name = first_name
        if last_name:
            new_member.last_name = last_name
        if jobs:
            for job in jobs:
                new_member.jobs.append(ndb.Key(db_models.Job, int(job)))
        key = new_member.put()
        out = new_member.to_dict()
        self.response.write(json.dumps(out))
        return

    def get(self, **kwargs):
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/JSON"
            return
        if 'id' in kwargs:
            out = ndb.Key(db_models.Member, int(kwargs['id'])).get().to_dict()
            self.response.write(json.dumps(out))
            return
        else:
            q = db_models.Member.query()
            keys = q.fetch(keys_only=True)
            results = {'keys': [x.id() for x in keys]}
            self.response.write(json.dumps(results))
            return


class MemberJobs(webapp2.RequestHandler):
    # This guy allows jobs to be added to existing members  
    def put(self, **kwargs):
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/json"
        if 'cid' in kwargs:
            member = ndb.Key(db_models.Member, int(kwargs['cid'])).get()
            if not member:
                self.response.status = 404
                self.response.status_message = "Member not found."
                return
        if 'bid' in kwargs:
            job = ndb.Key(db_models.Job, int(kwargs['bid']))
            if not member:
                self.response.status = 404
                self.response.status_message = "Member not found."
                return
        if job not in member.jobs:
            member.jobs.append(job)
            member.put()
        self.response.write(json.dumps(member.to_dict()))
        return


class MemberRemoveJob(webapp2.RequestHandler):
    # Remove a job from a member
    def put(self, **kwargs):
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/json"
        if 'cid' in kwargs:
            member = ndb.Key(db_models.Member, int(kwargs['cid'])).get()
            if not member:
                self.response.status = 404
                self.response.status_message = "Member not found."
                return
        if 'bid' in kwargs:
            job = ndb.Key(db_models.Job, int(kwargs['bid']))
            member.jobs.remove(job)
            member.put()
            
        self.response.write(json.dumps(member.to_dict()))
        return


class MemberSearch(webapp2.RequestHandler):
    def get(self):
        """
        Search for moderators
        POST Body Variables:
        title - String.  
        author - String.
        """
        if 'application/json' not in self.request.accept:
            self.response.status = 406
            self.response.status_message = "Not Acceptable, API only supports application/json MIME type"
            return
        q = db_models.Member.query()
        if self.request.get('user_name', None):
            q = q.filter(db_models.Member.user_name == self.request.get('user_name'))
        if self.request.get('last_name', None):
            q = q.filter(db_models.Member.last_name == self.request.get('last_name'))
        keys = q.fetch(keys_only=True)
        results = {'keys': [x.id() for x in keys]}
        self.response.write(json.dumps(results))

