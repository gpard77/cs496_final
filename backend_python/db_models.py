from google.appengine.ext import ndb


class Model(ndb.Model):
    def to_dict(self):
        d = super(Model, self).to_dict()
        d['key'] = self.key.id()
        return d


class Job(ndb.Model):
    caption = ndb.StringProperty()
    street = ndb.StringProperty()
    city = ndb.StringProperty()
    zip_code = ndb.StringProperty()
    drop = ndb.StringProperty()
    offer = ndb.StringProperty()


class Member(ndb.Model):
    user_name = ndb.StringProperty(required=True)
    passcode = ndb.StringProperty(required=True)
    first_name = ndb.StringProperty()
    last_name = ndb.StringProperty()
    jobs = ndb.KeyProperty(repeated=True)

    def to_dict(self):
        d = super(Member, self).to_dict()
        d['jobs'] = [j.id() for j in d['jobs']]
        return d
