import gensim
import os
import collections


#Tüm makaleleri bir araya getirip, paragraf halinde okur
def readDataset(files,articleNames): # read all articles
    corpus =[];
    for i, file in enumerate(files):
        count =0;
        with open(file) as f:
            for data in f:
                if 1 < len(data):
                    corpus.append(gensim.models.doc2vec.TaggedDocument(gensim.utils.simple_preprocess(data),[str(count)+" "+articleNames[i]]));
                    count = count+1;
    return corpus;


#Her bir makaleyi ayrı ayrı okur
def readArticle(fname):

    paragraphs =[];
    i =0;
    with open(fname) as f:
        for data  in f:
            if  2 < len(data):
                paragraphs.append(gensim.models.doc2vec.TaggedDocument(gensim.utils.simple_preprocess(data),[i]));

            i =i+1;

    return paragraphs;


def trainDataset(corpus):

    model = gensim.models.doc2vec.Doc2Vec(vector_size=500, min_count=1, epochs=100,dm=0,dbow_words=1,window=6);
    model.build_vocab(corpus);

    model.train(corpus, total_examples=model.corpus_count, epochs=model.epochs);

    return model;


# başarı oranını hesaplar
def calculateRank(dataset,model):
    ranks =[];
    second_ranks =[];
    for doc in dataset:
        inferred_vector =model.infer_vector(doc.words);
        sims =model.docvecs.most_similar([inferred_vector],topn=len(model.docvecs));
        rank =[docid for docid, sim in sims].index(doc.tags[0]);
        ranks.append(rank)
        second_ranks.append(sims[1]);

    t =collections.Counter(ranks);
    print(t);
    print("");


if __name__ == "__main__":

    path = 'dataset/articles/';
    modelPath ='dataset/models/';
    files = []
    articleNames =[];
    for r, d, f in os.walk(path):
        for file in f:
            articleNames.append(file);
            files.append(os.path.join(r, file))

    dataset = readDataset(files,articleNames);
    model =trainDataset(dataset);
    model.save(modelPath+"dataset.model");
    print("Dataset rank :");
    calculateRank(dataset,model);

    for f in articleNames:
        dataset =readArticle(path+f);
        model =trainDataset(dataset);
        model.save(modelPath+f+".model");
        print(f+" article rank :");
        calculateRank(dataset,model);





