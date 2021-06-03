from datetime import datetime
import concurrent.futures
import PySimpleGUI
import twitter
import tweepy
import random
import time
import os


class TweepyModule:

    def set_api(self):
        self.auth = tweepy.AppAuthHandler("ej9xM4fp6CXo9jxbn8HSfuaMX",
                                          "p1zqluAO67dSrpDHDDFAw6mZTxWTQy6acyWAShulaA07etIN3H")
        self.api = tweepy.API(self.auth)
        self.api_twitter_official = twitter.Api(consumer_key='ej9xM4fp6CXo9jxbn8HSfuaMX',
                                                consumer_secret='p1zqluAO67dSrpDHDDFAw6mZTxWTQy6acyWAShulaA07etIN3H',
                                                access_token_key='4165914892-Stzhsbf10jyoZBsylN7PurQTNGYh2K9WsdFRgOI',
                                                access_token_secret='SuBmCVz3csEqNp9fanOhi3mopfC2gBycghNr1yxwaGI7R')

    def popup(self,text):
        PySimpleGUI.popup(text)

    def get_ID(self):
        self.user_id = {}
        for user in self.users:
            try:
                user_id_to_add = self.api_twitter_official.UsersLookup(screen_name=user)[0].AsDict()['id']
                self.user_id[user] = user_id_to_add
            except:
                if len(self.users) == 1:
                    text = ("Warning, the twitter handle: %s,is invalid. As this is the only twitter handle given, the program must exit" % (user))
                    self.popup(text)
                    exit()
                else:
                    text = ("Warning, the twitter handle: %s is invalid" % (user))
                    self.popup(text)
                print(text)
                self.users.remove(user)
                continue

    def __init__(self, users, start, end):
        self.users, self.start, self.end = users, start, end
        self.set_api()
        self.get_ID()
        self.current_handle_index = 0
        self.all_tweets = {}
        self.run_tweet_getter()
        os.system('clear')
        print('Tweets have be downloaded')

    def run_tweet_getter(self):
        with concurrent.futures.ThreadPoolExecutor() as executor:
            future = {
                user: executor.submit(self.get_tweets, handle=user)
                for user in self.users
            }

            for user in self.users:
                self.all_tweets[user] = future[user].result()

    def retrieve_tweets(self):
        self.retrieved_tweets = []
        index = 0
        for user in self.users:
            tweets = self.api.user_timeline(screen_name=user, count=200)
            tweet0 = None
            for tweet in tweets:
                if tweet0 is None:
                    tweet0 = tweet
                index += 1

    def to_datetime(self, a):
        return datetime.fromtimestamp(a)

    def get_tweets(self, handle):
        start_day = self.to_datetime(self.start)
        total_tweets, times_slept, tweet_count = 0, 0, 0
        user_tweets = []
        user_id = self.user_id[handle]
        prev_tweet_time, earliest_tweet_id = None, None
        batch_num = 1
        while True:
            os.system('clear')
            print('Downloading batch %d of tweets for %s' % (batch_num,handle))
            batch_num = batch_num + 1
            try:
                if earliest_tweet_id == None:  # Runs on first go
                    timeline = self.api.user_timeline(id=user_id, count=200, wait_on_rate_limit=True)
                else:
                    timeline = self.api.user_timeline(id=user_id, max_id=earliest_tweet_id, wait_on_rate_limit=True)
            except Exception as e:
                timeline = []  # Handle errors from timeline not being created if an error is encountered in the previous try statement
                if e.response.status_code == 429:
                    times_slept += 1
                    if times_slept == 2:
                        break
                    print('Sleeping due to error')
                    time.sleep(60)
                    times_slept = 0
                else:
                    foo = None
            found_early_tweets = False
            for tweet in timeline:
                if tweet.created_at >= start_day:
                    if prev_tweet_time == tweet.created_at:
                        tweet_count += 1
                        if tweet_count == 3:
                            time.sleep(0.5)
                        else:
                            continue
                    total_tweets += 1
                    user_tweets.append(tweet)
                    prev_tweet_time = tweet.created_at
                else:
                    found_early_tweets = True
                if earliest_tweet_id is None or tweet.id < earliest_tweet_id:
                    earliest_tweet_id = tweet.id
            if found_early_tweets:
                return user_tweets
        return user_tweets
