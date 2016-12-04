# import webapp2
# from google.appengine.api import oauth

# application = webapp2.WSGIApplication([
#     ('/book', 'book.Book'),
#     ('/shelf', 'shelf.Shelf')
# ], debug=True)

# # get back details about a specific book - use an id
# application.router.add(webapp2.Route(r'/book/<id:[0-9]+><:/?>', 'book.Book'))
# # this one gets a list of book keys
# application.router.add(webapp2.Route(r'/book/search', 'book.BookSearch'))
# # this one gets a list of shelf keys
# application.router.add(webapp2.Route(r'/shelf', 'shelf.Shelf'))
# # this one allows book additions to a shelf via the PUT method
# application.router.add(webapp2.Route(r'/shelf/<cid:[0-9]+>/book/<bid:[0-9]+><:/?>', 'shelf.ShelfBooks'))
# # this one also returns a list of shelf keys
# application.router.add(webapp2.Route(r'/shelf/search', 'shelf.ShelfSearch'))
# # this one provides details about a specific shelf - use an id
# application.router.add(webapp2.Route(r'/shelf/<id:[0-9]+><:/?>', 'shelf.Shelf'))


import webapp2
from google.appengine.api import oauth

application = webapp2.WSGIApplication([
    ('/job', 'job.Job'),
    ('/member', 'member.Member')
], debug=True)

# get back details about a specific job - use an id
application.router.add(webapp2.Route(r'/job/<id:[0-9]+><:/?>', 'job.Job'))
# this one gets a list of job keys
application.router.add(webapp2.Route(r'/job/search', 'job.JobSearch'))
# this one gets a list of member keys
application.router.add(webapp2.Route(r'/member', 'member.Member'))
# this one allows job additions to a member via the PUT method
application.router.add(webapp2.Route(r'/member/<cid:[0-9]+>/job/<bid:[0-9]+><:/?>', 'member.MemberJobs'))
# remove job from member
application.router.add(webapp2.Route(r'/member/<cid:[0-9]+>/job/remove/<bid:[0-9]+><:/?>', 'member.MemberRemoveJob'))
# this one also returns a list of member keys
application.router.add(webapp2.Route(r'/member/search', 'member.MemberSearch'))
# this one provides details about a specific member - use an id
application.router.add(webapp2.Route(r'/member/<id:[0-9]+><:/?>', 'member.Member'))

